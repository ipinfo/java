package io.ipinfo.api.request;

import io.ipinfo.api.errors.ErrorResponseException;
import io.ipinfo.api.errors.InvalidTokenException;
import io.ipinfo.api.errors.RateLimitedException;
import io.ipinfo.api.model.IPResponse;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class IPRequest extends BaseRequest<IPResponse> {
    private final static String URL_FORMAT = "https://ipinfo.io/%s";
    private final String ip;

    public IPRequest(OkHttpClient client, String token, String ip) {
        super(client, token);
        this.ip = ip;
    }

    @Override
    public IPResponse handle() throws RateLimitedException, InvalidTokenException {
        if (isBogon(ip)) {
            try {
                return new IPResponse(ip, true);
            } catch (Exception ex) {
                throw new ErrorResponseException(ex);
            }
        }

        String url = String.format(URL_FORMAT, ip);
        Request.Builder request = new Request.Builder().url(url).get();

        try (Response response = handleRequest(request)) {
            if (response == null || response.body() == null) {
                return null;
            }

            try {
                return gson.fromJson(response.body().string(), IPResponse.class);
            } catch (Exception ex) {
                throw new ErrorResponseException(ex);
            }
        }
    }

    static IpAddressMatcher[] IpAddressMatcherList = {
        // IPv4
        new IpAddressMatcher("0.0.0.0/8"),
        new IpAddressMatcher("10.0.0.0/8"),
        new IpAddressMatcher("100.64.0.0/10"),
        new IpAddressMatcher("127.0.0.0/8"),
        new IpAddressMatcher("169.254.0.0/16"),
        new IpAddressMatcher("172.16.0.0/12"),
        new IpAddressMatcher("192.0.0.0/24"),
        new IpAddressMatcher("192.0.2.0/24"),
        new IpAddressMatcher("192.168.0.0/16"),
        new IpAddressMatcher("198.18.0.0/15"),
        new IpAddressMatcher("198.51.100.0/24"),
        new IpAddressMatcher("203.0.113.0/24"),
        new IpAddressMatcher("224.0.0.0/4"),
        new IpAddressMatcher("240.0.0.0/4"),
        new IpAddressMatcher("255.255.255.255/32"),
        // IPv6
        new IpAddressMatcher("::/128"),
        new IpAddressMatcher("::1/128"),
        new IpAddressMatcher("::ffff:0:0/96"),
        new IpAddressMatcher("::/96"),
        new IpAddressMatcher("100::/64"),
        new IpAddressMatcher("2001:10::/28"),
        new IpAddressMatcher("2001:db8::/32"),
        new IpAddressMatcher("fc00::/7"),
        new IpAddressMatcher("fe80::/10"),
        new IpAddressMatcher("fec0::/10"),
        new IpAddressMatcher("ff00::/8"),
        // 6to4
        new IpAddressMatcher("2002::/24"),
        new IpAddressMatcher("2002:a00::/24"),
        new IpAddressMatcher("2002:7f00::/24"),
        new IpAddressMatcher("2002:a9fe::/32"),
        new IpAddressMatcher("2002:ac10::/28"),
        new IpAddressMatcher("2002:c000::/40"),
        new IpAddressMatcher("2002:c000:200::/40"),
        new IpAddressMatcher("2002:c0a8::/32"),
        new IpAddressMatcher("2002:c612::/31"),
        new IpAddressMatcher("2002:c633:6400::/40"),
        new IpAddressMatcher("2002:cb00:7100::/40"),
        new IpAddressMatcher("2002:e000::/20"),
        new IpAddressMatcher("2002:f000::/20"),
        new IpAddressMatcher("2002:ffff:ffff::/48"),
        // Teredo
        new IpAddressMatcher("2001::/40"),
        new IpAddressMatcher("2001:0:a00::/40"),
        new IpAddressMatcher("2001:0:7f00::/40"),
        new IpAddressMatcher("2001:0:a9fe::/48"),
        new IpAddressMatcher("2001:0:ac10::/44"),
        new IpAddressMatcher("2001:0:c000::/56"),
        new IpAddressMatcher("2001:0:c000:200::/56"),
        new IpAddressMatcher("2001:0:c0a8::/48"),
        new IpAddressMatcher("2001:0:c612::/47"),
        new IpAddressMatcher("2001:0:c633:6400::/56"),
        new IpAddressMatcher("2001:0:cb00:7100::/56"),
        new IpAddressMatcher("2001:0:e000::/36"),
        new IpAddressMatcher("2001:0:f000::/36"),
        new IpAddressMatcher("2001:0:ffff:ffff::/64")
    };

    static boolean isBogon(String ip)  {
        for (int i = 0; i < IpAddressMatcherList.length; i++) {
            IpAddressMatcher ipAddressMatcher = IpAddressMatcherList[i];
            if (ipAddressMatcher.matches(ip)) {
                return true;
            }
        }
        return false;
    }

    static InetAddress parseAddress(String address) {
        try {
            return InetAddress.getByName(address);
        }
        catch (UnknownHostException e) {
            throw new IllegalArgumentException("Failed to parse address: " + address, e);
        }
    }

    /*
    * Copyright 2002-2019 the original author or authors.
    *
    * Licensed under the Apache License, Version 2.0 (the "License");
    * you may not use this file except in compliance with the License.
    * You may obtain a copy of the License at
    *
    *      https://www.apache.org/licenses/LICENSE-2.0
    *
    * Unless required by applicable law or agreed to in writing, software
    * distributed under the License is distributed on an "AS IS" BASIS,
    * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    * See the License for the specific language governing permissions and
    * limitations under the License.
    */
    public static class IpAddressMatcher {
        private final int nMaskBits;
        private final InetAddress requiredAddress;

        public IpAddressMatcher(String ipAddress) {

            if (ipAddress.indexOf('/') > 0) {
                String[] addressAndMask = ipAddress.split("/");
                ipAddress = addressAndMask[0];
                nMaskBits = Integer.parseInt(addressAndMask[1]);
            }
            else {
                nMaskBits = -1;
            }
            requiredAddress = parseAddress(ipAddress);
        }

        public boolean matches(String address) {
            InetAddress remoteAddress = parseAddress(address);

            if (!requiredAddress.getClass().equals(remoteAddress.getClass())) {
                return false;
            }

            if (nMaskBits < 0) {
                return remoteAddress.equals(requiredAddress);
            }

            byte[] remAddr = remoteAddress.getAddress();
            byte[] reqAddr = requiredAddress.getAddress();

            int nMaskFullBytes = nMaskBits / 8;
            byte finalByte = (byte) (0xFF00 >> (nMaskBits & 0x07));

            for (int i = 0; i < nMaskFullBytes; i++) {
                if (remAddr[i] != reqAddr[i]) {
                    return false;
                }
            }

            if (finalByte != 0) {
                return (remAddr[nMaskFullBytes] & finalByte) == (reqAddr[nMaskFullBytes] & finalByte);
            }

            return true;
        }
    }
}
