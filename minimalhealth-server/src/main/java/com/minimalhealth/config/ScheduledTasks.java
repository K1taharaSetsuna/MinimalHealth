package com.minimalhealth.config;

import com.minimalhealth.model.entity.DailyStats;
import com.minimalhealth.model.entity.User;
import com.minimalhealth.repository.DailyStatsRepository;
import com.minimalhealth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduledTasks {

    private final UserRepository userRepository;
    private final DailyStatsRepository dailyStatsRepository;

    /**
     * Run at 00:05 every day to ensure all users have a daily_stats row for today.
     */
    @Scheduled(cron = "0 5 0 * * *")
    public void initDailyStats() {
        LocalDate today = LocalDate.now();
        log.info("开始初始化每日统计数据: {}", today);

        List<User> users = userRepository.findAll();
        int created = 0;
        for (User user : users) {
            if (dailyStatsRepository.findByUserIdAndRecordDate(user.getId(), today).isEmpty()) {
                dailyStatsRepository.save(DailyStats.builder()
                    .user(user).recordDate(today).build());
                created++;
            }
        }
        log.info("每日统计数据初始化完成: {}/{} 用户需要新建", created, users.size());
    }
}
