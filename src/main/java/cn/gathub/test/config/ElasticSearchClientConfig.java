package cn.gathub.test.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticSearchClientConfig {
  @Bean
  public RestHighLevelClient restHighLevelClient() {
    return new RestHighLevelClient(
        // 集群则配置多个
        RestClient.builder(
//            new HttpHost("localhost", 9201, "http"),
            new HttpHost("localhost", 9200, "http")));
  }
}
