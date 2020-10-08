package br.com.globo.starwars;

import br.com.globo.starwars.interceptor.RestTemplateHeaderModifierInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@EnableCaching
@SpringBootApplication
public class StarwarsApplication {

	public static void main(String[] args) {
		SpringApplication.run(StarwarsApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();

		List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();

		if (CollectionUtils.isEmpty(interceptors)) {
			interceptors = new ArrayList<>();
		}

		interceptors.add(new RestTemplateHeaderModifierInterceptor());
		restTemplate.setInterceptors(interceptors);

		return restTemplate;
	}
}
