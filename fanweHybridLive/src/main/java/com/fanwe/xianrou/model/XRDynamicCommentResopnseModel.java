package com.fanwe.xianrou.model;

import com.fanwe.hybrid.model.BaseActModel;

import java.util.List;

/**
 * @包名 com.fanwe.xianrou.model
 * @描述
 * @作者 Su
 * @创建时间 2017/4/15 16:12
 **/
public class XRDynamicCommentResopnseModel extends BaseActModel
{

    /**
     * digg : []
     * comment : {"weibo_id":268,"weibo_user_id":"167314","user_id":167314,"content":"好的好的好","type":1,"create_time":"2017-04-15 15:37:19","is_audit":1,"storey":1,"comment_id":884,"nick_name":"FZ-售后-李金滨","head_image":"http://liveimage.fanwe.net/public/attachment/201703/19/11/origin/1489866042167314.jpg","left_time":"刚刚","to_nick_name":""}
     * error : 评论发表成功!
     * comment_id : 0
     * status : 1
     * comment_count : 1
     */

    private CommentBean comment;
    private String error;
    private int comment_id;
    private int status;
    private int comment_count;
    private List<?> digg;

    public CommentBean getComment()
    {
        return comment;
    }

    public void setComment(CommentBean comment)
    {
        this.comment = comment;
    }

    public String getError()
    {
        return error;
    }

    public void setError(String error)
    {
        this.error = error;
    }

    public int getComment_id()
    {
        return comment_id;
    }

    public void setComment_id(int comment_id)
    {
        this.comment_id = comment_id;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public int getComment_count()
    {
        return comment_count;
    }

    public void setComment_count(int comment_count)
    {
        this.comment_count = comment_count;
    }

    public List<?> getDigg()
    {
        return digg;
    }

    public void setDigg(List<?> digg)
    {
        this.digg = digg;
    }

    public static class CommentBean
    {
        /**
         * weibo_id : 268
         * weibo_user_id : 167314
         * user_id : 167314
         * content : 好的好的好
         * type : 1
         * create_time : 2017-04-15 15:37:19
         * is_audit : 1
         * storey : 1
         * comment_id : 884
         * nick_name : FZ-售后-李金滨
         * head_image : http://liveimage.fanwe.net/public/attachment/201703/19/11/origin/1489866042167314.jpg
         * left_time : 刚刚
         * to_nick_name :
         */

        private int weibo_id;
        private String weibo_user_id;
        private String user_id;
        private String content;
        private int type;
        private String create_time;
        private int is_audit;
        private int storey;
        private String comment_id;
        private String nick_name;
        private String head_image;
        private String left_time;
        private String to_nick_name;

        public int getWeibo_id()
        {
            return weibo_id;
        }

        public void setWeibo_id(int weibo_id)
        {
            this.weibo_id = weibo_id;
        }

        public String getWeibo_user_id()
        {
            return weibo_user_id;
        }

        public void setWeibo_user_id(String weibo_user_id)
        {
            this.weibo_user_id = weibo_user_id;
        }

        public String getUser_id()
        {
            return user_id;
        }

        public void setUser_id(String user_id)
        {
            this.user_id = user_id;
        }

        public String getContent()
        {
            return content;
        }

        public void setContent(String content)
        {
            this.content = content;
        }

        public int getType()
        {
            return type;
        }

        public void setType(int type)
        {
            this.type = type;
        }

        public String getCreate_time()
        {
            return create_time;
        }

        public void setCreate_time(String create_time)
        {
            this.create_time = create_time;
        }

        public int getIs_audit()
        {
            return is_audit;
        }

        public void setIs_audit(int is_audit)
        {
            this.is_audit = is_audit;
        }

        public int getStorey()
        {
            return storey;
        }

        public void setStorey(int storey)
        {
            this.storey = storey;
        }

        public String getComment_id()
        {
            return comment_id;
        }

        public void setComment_id(String comment_id)
        {
            this.comment_id = comment_id;
        }

        public String getNick_name()
        {
            return nick_name;
        }

        public void setNick_name(String nick_name)
        {
            this.nick_name = nick_name;
        }

        public String getHead_image()
        {
            return head_image;
        }

        public void setHead_image(String head_image)
        {
            this.head_image = head_image;
        }

        public String getLeft_time()
        {
            return left_time;
        }

        public void setLeft_time(String left_time)
        {
            this.left_time = left_time;
        }

        public String getTo_nick_name()
        {
            return to_nick_name;
        }

        public void setTo_nick_name(String to_nick_name)
        {
            this.to_nick_name = to_nick_name;
        }

        @Override
        public String toString()
        {
            return "CommentBean{" +
                    "weibo_id=" + weibo_id +
                    ", weibo_user_id='" + weibo_user_id + '\'' +
                    ", user_id='" + user_id + '\'' +
                    ", content='" + content + '\'' +
                    ", type=" + type +
                    ", create_time='" + create_time + '\'' +
                    ", is_audit=" + is_audit +
                    ", storey=" + storey +
                    ", comment_id='" + comment_id + '\'' +
                    ", nick_name='" + nick_name + '\'' +
                    ", head_image='" + head_image + '\'' +
                    ", left_time='" + left_time + '\'' +
                    ", to_nick_name='" + to_nick_name + '\'' +
                    '}';
        }
    }
}
