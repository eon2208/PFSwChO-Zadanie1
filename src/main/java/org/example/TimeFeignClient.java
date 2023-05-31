package org.example;

import feign.Param;
import feign.RequestLine;

public interface TimeFeignClient {

    @RequestLine("GET /{ip}")
    GeoLocation findByIp(@Param("ip") String ip);
}
