package com.minimalhealth.repository;

import com.minimalhealth.model.entity.SmsCode;
import com.minimalhealth.model.enums.SmsPurpose;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.Optional;

public interface SmsCodeRepository extends JpaRepository<SmsCode, Long> {
    @Query("SELECT s FROM SmsCode s WHERE s.phone = :phone AND s.code = :code AND s.purpose = :purpose AND s.used = false AND s.expiresAt > :now")
    Optional<SmsCode> findValidCode(
        @Param("phone") String phone,
        @Param("code") String code,
        @Param("purpose") SmsPurpose purpose,
        @Param("now") LocalDateTime now
    );

    long countByPhoneAndCreatedAtAfter(String phone, LocalDateTime after);
}
