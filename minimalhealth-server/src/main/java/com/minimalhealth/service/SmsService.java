package com.minimalhealth.service;

import com.minimalhealth.exception.BusinessException;
import com.minimalhealth.model.entity.SmsCode;
import com.minimalhealth.model.enums.SmsPurpose;
import com.minimalhealth.repository.SmsCodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsService {

    private final SmsCodeRepository smsCodeRepository;

    @Value("${app.sms.code-length}")
    private int codeLength;

    @Value("${app.sms.code-expiration-minutes}")
    private int codeExpirationMinutes;

    @Value("${app.sms.rate-limit-seconds}")
    private int rateLimitSeconds;

    public void sendCode(String phone, SmsPurpose purpose) {
        // Rate limit check
        LocalDateTime rateLimitAfter = LocalDateTime.now().minusSeconds(rateLimitSeconds);
        long recentCount = smsCodeRepository.countByPhoneAndCreatedAtAfter(phone, rateLimitAfter);
        if (recentCount > 0) {
            throw new BusinessException(429, "发送过于频繁，请稍后再试");
        }

        // Generate code
        String code = generateCode();

        // Save to DB
        SmsCode smsCode = SmsCode.builder()
            .phone(phone)
            .code(code)
            .purpose(purpose)
            .expiresAt(LocalDateTime.now().plusMinutes(codeExpirationMinutes))
            .build();
        smsCodeRepository.save(smsCode);

        // Mock: print to console
        log.info("===== 短信验证码 ({}): {} -> {} =====", purpose, phone, code);
    }

    public void validateCode(String phone, String code, SmsPurpose purpose) {
        SmsCode validCode = smsCodeRepository.findValidCode(
            phone, code, purpose, LocalDateTime.now()
        ).orElseThrow(() -> new BusinessException("验证码错误或已过期"));

        validCode.setUsed(true);
        smsCodeRepository.save(validCode);
    }

    private String generateCode() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < codeLength; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}
