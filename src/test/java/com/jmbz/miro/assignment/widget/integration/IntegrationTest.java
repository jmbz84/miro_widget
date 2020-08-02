package com.jmbz.miro.assignment.widget.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmbz.miro.assignment.widget.model.Widget;
import org.junit.After;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate=new TestRestTemplate();


    @After
    public  void clean() throws Exception{
        final String baseUrlDelete = "http://localhost:"+port+"/widgets/";
        URI uriDelete = new URI(baseUrlDelete);
        this.restTemplate.delete(uriDelete);
    }

    @Test
    public void insertMultipleWidgetVerify() throws Exception{
        final String baseUrl = "http://localhost:"+port+"/widgets/";
        URI uri = new URI(baseUrl);
        List<Widget> widgetList=new ArrayList<>();
        widgetList.add(getWidget(null,1,1,100,100));
        widgetList.add(getWidget(null,2,2,100,100));
        widgetList.add(getWidget(null,3,3,100,100));
        HttpEntity<List> request = new HttpEntity<>(widgetList);

        ResponseEntity<List> result = this.restTemplate.postForEntity(uri,request,List.class);

        //Verify request succeed
        Assert.assertEquals(201, result.getStatusCodeValue());

        result = this.restTemplate.getForEntity(uri,List.class);

        ObjectMapper mapper = new ObjectMapper();
        List<Widget> resultList = mapper.convertValue(result.getBody(), new TypeReference<List<Widget>>() { });


        Assert.assertEquals(200, result.getStatusCodeValue());
        Assert.assertEquals(3,result.getBody().size());
        Assert.assertEquals(-2,(int)(resultList.get(0)).getZIndex());
        Assert.assertEquals(-1,(int)(resultList.get(1)).getZIndex());
        Assert.assertEquals(0,(int)(resultList.get(2)).getZIndex());

        Assert.assertEquals(3,(int)(resultList.get(0)).getX());
        Assert.assertEquals(2,(int)(resultList.get(1)).getX());
        Assert.assertEquals(1,(int)(resultList.get(2)).getX());

    }

    @Test
    public void getWidgetsFilteredVerify() throws Exception{
        final String baseUrl = "http://localhost:"+port+"/widgets/";
        URI uri = new URI(baseUrl);
        List<Widget> widgetList=new ArrayList<>();
        widgetList.add(getWidget(null,50,50,100,100));
        widgetList.add(getWidget(null,50,100,100,100));
        widgetList.add(getWidget(null,100,100,100,100));
        HttpEntity<List> request = new HttpEntity<>(widgetList);
        ResponseEntity<List> result = this.restTemplate.postForEntity(uri,request,List.class);

        //Verify request succeed
        Assert.assertEquals(201, result.getStatusCodeValue());

        final String baseUrlFilter = "http://localhost:"+port+"/widgets/filter/0/0/100/150";
        URI uriFilter = new URI(baseUrlFilter);
        result = this.restTemplate.getForEntity(uriFilter,List.class);

        ObjectMapper mapper = new ObjectMapper();
        List<Widget> resultList = mapper.convertValue(result.getBody(), new TypeReference<List<Widget>>() { });


        Assert.assertEquals(200, result.getStatusCodeValue());
        Assert.assertEquals(2,result.getBody().size());
        Assert.assertEquals(50,(int)(resultList.get(0)).getX());
        Assert.assertEquals(100,(int)(resultList.get(0)).getY());

        Assert.assertEquals(50,(int)(resultList.get(1)).getX());
        Assert.assertEquals(50,(int)(resultList.get(1)).getY());

    }

    @Test
    public void getWidgetsPagedVerify() throws Exception{
        String baseUrl = "http://localhost:"+port+"/widgets/";
        URI uri = new URI(baseUrl);
        List<Widget> widgetList=new ArrayList<>();

        for(int i = 0; i<100; i++){
            widgetList.add(getWidget(null,i+50,50,100,100));
        }
        HttpEntity<List> request = new HttpEntity<>(widgetList);
        ResponseEntity<List> result = this.restTemplate.postForEntity(uri,request,List.class);

        Assert.assertEquals(201, result.getStatusCodeValue());
        Assert.assertEquals(100, result.getBody().size());

        String baseUrlPage = "http://localhost:"+port+"/widgets/page/?page=0&size=5";
        URI uriPage = new URI(baseUrlPage);
        ResponseEntity resultPage0 = this.restTemplate.getForEntity(uriPage, Object.class);

        List<Widget> listPage0=((ArrayList)((HashMap)resultPage0.getBody()).get("content"));
        Assert.assertEquals(200, resultPage0.getStatusCodeValue());
        Assert.assertEquals(5,listPage0.size());

        baseUrlPage = "http://localhost:"+port+"/widgets/page/?page=1&size=5";
        uriPage = new URI(baseUrlPage);
        ResponseEntity resultPage1= this.restTemplate.getForEntity(uriPage,Object.class);
        List<Widget> listPage1=((ArrayList)((HashMap)resultPage1.getBody()).get("content"));
        Assert.assertEquals(200, resultPage1.getStatusCodeValue());
        Assert.assertEquals(5,listPage1.size());

        //No elements in common
        Assert.assertTrue(Collections.disjoint(listPage1, listPage0));
    }

    @Test
    public void getWidgetsEmptyPageVerify() throws Exception{
        String baseUrl = "http://localhost:"+port+"/widgets/";
        URI uri = new URI(baseUrl);
        List<Widget> widgetList=new ArrayList<>();

        for(int i = 0; i<100; i++){
            widgetList.add(getWidget(null,i+50,50,100,100));
        }
        HttpEntity<List> request = new HttpEntity<>(widgetList);
        ResponseEntity<List> result = this.restTemplate.postForEntity(uri,request,List.class);

        Assert.assertEquals(201, result.getStatusCodeValue());
        Assert.assertEquals(100, result.getBody().size());

        String baseUrlPage = "http://localhost:"+port+"/widgets/page/?page=0&size=100";
        URI uriPage = new URI(baseUrlPage);
        ResponseEntity resultPage0 = this.restTemplate.getForEntity(uriPage, Object.class);

        List<Widget> listPage0=((ArrayList)((HashMap)resultPage0.getBody()).get("content"));
        Assert.assertEquals(200, resultPage0.getStatusCodeValue());
        Assert.assertEquals(100,listPage0.size());

        baseUrlPage = "http://localhost:"+port+"/widgets/page/?page=1&size=100";
        uriPage = new URI(baseUrlPage);
        ResponseEntity resultPage1= this.restTemplate.getForEntity(uriPage,Object.class);
        List<Widget> listPage1=((ArrayList)((HashMap)resultPage1.getBody()).get("content"));
        Assert.assertEquals(200, resultPage1.getStatusCodeValue());
        Assert.assertEquals(0,listPage1.size());

    }

    Widget getWidget(Integer zIndex,Integer x,Integer y,Integer height,Integer width){
        Widget widget1=new Widget();
        widget1.setHeight(height);
        widget1.setWidth(width);
        widget1.setX(x);
        widget1.setY(y);
        widget1.setZIndex(zIndex);
        return widget1;
    }
}
