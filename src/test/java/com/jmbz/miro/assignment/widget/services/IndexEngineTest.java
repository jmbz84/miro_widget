package com.jmbz.miro.assignment.widget.services;

import com.jmbz.miro.assignment.widget.model.IndexNode;
import org.junit.Assert;
import org.junit.Test;

public class IndexEngineTest {

    IndexEngine indexEngine=new IndexEngine();

    @Test
    public void insertNullZIndexVerify() {

        IndexNode indexNode=new IndexNode("id",null);
        IndexNode insertedIndexNode=indexEngine.insertNode(indexNode);
        Assert.assertTrue(insertedIndexNode.getZIndex()==0);
        Assert.assertEquals(1, (int) indexEngine.getList().size());
    }

    @Test
    public void insertZIndexVerify() {

        IndexNode indexNode=new IndexNode("id",1);
        IndexNode insertedIndexNode=indexEngine.insertNode(indexNode);
        Assert.assertEquals(1, (int) insertedIndexNode.getZIndex());
        Assert.assertEquals(1, (int) indexEngine.getList().size());
    }

    @Test
    public void insertMultipleNullZIndexVerify() {

        IndexNode indexNode=new IndexNode("id1",null);
        IndexNode indexNode2=new IndexNode("id2",null);
        IndexNode indexNode3=new IndexNode("id3",null);
        indexEngine.insertNode(indexNode);
        indexEngine.insertNode(indexNode2);
        indexEngine.insertNode(indexNode3);

        Assert.assertEquals(-2,(int)indexEngine.getList().get(0).getZIndex());
        Assert.assertEquals("id3",indexEngine.getList().get(0).getId());

        Assert.assertEquals(-1,(int)indexEngine.getList().get(1).getZIndex());
        Assert.assertEquals("id2",indexEngine.getList().get(1).getId());

        Assert.assertEquals(0,(int)indexEngine.getList().get(2).getZIndex());
        Assert.assertEquals("id1",indexEngine.getList().get(2).getId());
    }

    @Test
    public void insertMultipleZIndexVerify() {

        IndexNode indexNode=new IndexNode("id1",0);
        IndexNode indexNode2=new IndexNode("id2",1);
        IndexNode indexNode3=new IndexNode("id3",-1);
        indexEngine.insertNode(indexNode);
        indexEngine.insertNode(indexNode2);
        indexEngine.insertNode(indexNode3);

        Assert.assertEquals(-1,(int)indexEngine.getList().get(0).getZIndex());
        Assert.assertEquals("id3",indexEngine.getList().get(0).getId());

        Assert.assertEquals(0,(int)indexEngine.getList().get(1).getZIndex());
        Assert.assertEquals("id1",indexEngine.getList().get(1).getId());

        Assert.assertEquals(1,(int)indexEngine.getList().get(2).getZIndex());
        Assert.assertEquals("id2",indexEngine.getList().get(2).getId());
    }

    @Test
    public void updateNodeVerify(){

        IndexNode indexNode=new IndexNode("id1",0);
        IndexNode indexNode2=new IndexNode("id1",1);
        indexEngine.insertNode(indexNode);
        indexEngine.updateNode(indexNode2);
        Assert.assertEquals(1,(int)indexEngine.getList().get(0).getZIndex());
        Assert.assertEquals("id1",indexEngine.getList().get(0).getId());
    }

    @Test
    public void deleteNodeVerify(){

        IndexNode indexNode=new IndexNode("id1",0);
        IndexNode indexNode2=new IndexNode("id2",1);
        IndexNode indexNode3=new IndexNode("id3",-1);
        indexEngine.insertNode(indexNode);
        indexEngine.insertNode(indexNode2);
        indexEngine.insertNode(indexNode3);
        indexEngine.deleteNode("id2");
        Assert.assertEquals(-1,(int)indexEngine.getList().get(0).getZIndex());
        Assert.assertEquals("id3",indexEngine.getList().get(0).getId());

        Assert.assertEquals(0,(int)indexEngine.getList().get(1).getZIndex());
        Assert.assertEquals("id1",indexEngine.getList().get(1).getId());

        Assert.assertEquals(2,(int)indexEngine.getList().size());

    }
}