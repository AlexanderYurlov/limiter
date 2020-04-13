package com.alex.limiter.config.interceptor;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SessionData {

    private String ip;

    private Long time;

}
