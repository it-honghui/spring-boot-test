package cn.gathub.test.es;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

import java.io.IOException;

import cn.gathub.test.model.User;
import cn.hutool.json.JSONUtil;

@SpringBootTest
class ElasticSearchTest {

  @Autowired
  private ElasticsearchRestTemplate esTemplate; // 把RestHighLevelClient做了一层封装

  /**
   * 创建索引
   */
  @Test
  public void testCreateIndex() {
    boolean flag = esTemplate.createIndex("user_index");
    Assert.assertTrue(flag);
  }

  /**
   * 判断索引所否存在
   */
  @Test
  public void testExistIndex() {
    boolean flag = esTemplate.indexExists("user_index");
    Assert.assertTrue(flag);
  }

  /**
   * 测试删除索引
   */
  @Test
  public void testDeleteIndex() {
    boolean flag = esTemplate.deleteIndex("user_index");
    Assert.assertTrue(flag);
  }

  /**
   * 测试添加文档
   */
  @Test
  public void testAddDocument() throws IOException {
    RestHighLevelClient client = esTemplate.getClient();
    User user = new User("弘辉", 25);
    IndexRequest request = new IndexRequest("user_index");
    request.id("6");
    request.timeout(TimeValue.timeValueSeconds(1));
    request.source(JSONUtil.toJsonStr(user), XContentType.JSON);

    IndexResponse response = client.index(request, RequestOptions.DEFAULT);
    Assert.assertEquals(response.status().toString(), "CREATED");
  }

}