package cn.gathub.test.es;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.gathub.test.model.User;
import cn.hutool.json.JSONUtil;

@SpringBootTest
class ElasticSearchClientConfigTest {
  @Autowired
  private RestHighLevelClient restHighLevelClient;

  /**
   * 创建索引
   */
  @Test
  public void testCreateIndex() throws IOException {
    // 1、创建索引的请求
    CreateIndexRequest request = new CreateIndexRequest("user_index");
    // 2、执行请求
    CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
    Assert.assertEquals(createIndexResponse.index(), "user_index");
  }

  /**
   * 判断索引所否存在
   */
  @Test
  public void testExistIndex() throws IOException {
    GetIndexRequest request = new GetIndexRequest("user_index");
    boolean flag = restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
    Assert.assertTrue(flag);
  }


  /**
   * 测试删除索引
   */
  @Test
  public void testDeleteIndex() throws IOException {
    DeleteIndexRequest request = new DeleteIndexRequest("user_index");
    AcknowledgedResponse acknowledgedResponse = restHighLevelClient.indices().delete(request, RequestOptions.DEFAULT);
    Assert.assertTrue(acknowledgedResponse.isAcknowledged());
  }


  /**
   * 测试添加文档
   */
  @Test
  public void testAddDocument() throws IOException {
    User user = new User("HongHui", 25);
    IndexRequest request = new IndexRequest("user_index");
    request.id("1");
    request.timeout(TimeValue.timeValueSeconds(1));
    request.source(JSONUtil.toJsonStr(user), XContentType.JSON);

    IndexResponse response = restHighLevelClient.index(request, RequestOptions.DEFAULT);
    Assert.assertEquals(response.status().toString(), "CREATED");
  }

  /**
   * 判断文档是否存在
   */
  @Test
  public void testIsExistDocument() throws IOException {
    GetRequest request = new GetRequest("user_index", "1");
    request.fetchSourceContext(new FetchSourceContext(false));
    request.storedFields("_none_");

    boolean flag = restHighLevelClient.exists(request, RequestOptions.DEFAULT);
    Assert.assertTrue(flag);
  }

  /**
   * 获取文档内容
   */
  @Test
  public void testGetDocument() throws IOException {
    GetRequest request = new GetRequest("user_index", "1");
    GetResponse response = restHighLevelClient.get(request, RequestOptions.DEFAULT);
    System.out.println(response.getSourceAsString());
    System.out.println(response);
  }

  /**
   * 修改文档内容
   */
  @Test
  public void testUpdateDocument() throws IOException {
    UpdateRequest request = new UpdateRequest("user_index", "1");
    request.timeout("1s");

    User user = new User("弘辉", 18);
    request.doc(JSONUtil.toJsonStr(user), XContentType.JSON);

    UpdateResponse response = restHighLevelClient.update(request, RequestOptions.DEFAULT);
    System.out.println(response.status());
  }

  /**
   * 删除文档内容
   */
  @Test
  public void testDeleteDocument() throws IOException {
    DeleteRequest request = new DeleteRequest("user_index", "1");
    request.timeout("1s");

    DeleteResponse response = restHighLevelClient.delete(request, RequestOptions.DEFAULT);
    System.out.println(response.status());
  }

  /**
   * 批量插入
   */
  @Test
  public void testBulkAddDocument() throws IOException {
    BulkRequest request = new BulkRequest("user_index");
    request.timeout("10s");

    List<User> userList = new ArrayList<>();
    userList.add(new User("HongHui", 18));
    userList.add(new User("HongHui2", 19));
    userList.add(new User("HongHui3", 20));
    userList.add(new User("HongHui4", 21));
    userList.add(new User("HongHui5", 22));

    for (int i = 0; i < userList.size(); i++) {
      // 批量更新和删除在这里修改对应的请求
      request.add(
          new IndexRequest("user_index")
              .id("" + (i + 1))
              .source(JSONUtil.toJsonStr(userList.get(i)), XContentType.JSON)
      );
    }

    BulkResponse response = restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
    System.out.println(response.status());
  }

  /**
   * 自定义查询
   */
  @Test
  public void testSearchDocument() throws IOException {
    SearchRequest request = new SearchRequest("user_index");

    // 构建查询条件
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

    // 查询条件
    TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("name.keyword", "HongHui3");
    searchSourceBuilder.query(termQueryBuilder);
    searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

    request.source(searchSourceBuilder);

    SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
    for (SearchHit hit : response.getHits().getHits()) {
      System.out.println(hit.getSourceAsMap());
    }

  }


}