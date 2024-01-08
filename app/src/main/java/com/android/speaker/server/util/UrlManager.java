package com.android.speaker.server.util;

public class UrlManager {
    private static final String BASE_URL = "http://talkease.top/speakEnglish";
    public static String formatUrl(String url) {
        if(url == null || "".equals(url)) {
            return "";
        }
        return url.startsWith("http") ? url : BASE_URL + url;
    }

    public static final String WEBSOCKET_URL = "ws://talkease.top/speakEnglish/speakEnglish-ums/webSocket/openSpeak";

    /**
     * 登录
     */
    public static final String USER_LOGIN = "/speakEnglish-auth/oauth/token";

    /**
     * 获取验证码
     */
    public static final String GET_CAPTCHA = "/speakEnglish-auth/sms-code";

    /**
     * 获取用户信息
     */
    public static final String GET_USER_INFO = "/speakEnglish-ums/app-api/v1/user/userInfo";

    /**
     * 获取学习信息
     */
    public static final String GET_LEARN_INFO = "/speakEnglish-ums/app-api/v1/user/userLearnInfo";

    /**
     * 精品课程首页推荐
     */
    public static final String GET_RECOMMEND_COURSE_LIST = "/speakEnglish-ums/app-api/v1/project/recommendList";
    /**
     * 开口说分页数据
     */
    public static final String GET_OPEN_SPEAKER_LIST = "/speakEnglish-ums/app-api/v1/project/listOpenSpeak";

    /**
     * 课程主类目录
     */
    public static final String GET_CATEGORY_LIST = "/speakEnglish-ums/app-api/v1/project/listAllCategories";

    /**
     * 通过主类获取课堂信息
     */
    public static final String GET_COURSE_LIST_BY_MAIN = "/speakEnglish-ums/app-api/v1/project/listByMain";

    /**
     * 精品课程分页数据
     */
    public static final String GET_QUALITY_COURSE_LIST = "/speakEnglish-ums/app-api/v1/project/list";

    /**
     * 专项课程分页数据
     */
    public static final String GET_SPECIAL_COURSE_LIST = "/speakEnglish-ums/app-api/v1/course/specialization";

    /**
     * 通过子类获取课堂信息
     */
    public static final String GET_COURSE_LIST_BY_SUB = "/speakEnglish-ums/app-api/v1/project/listBySub";

    /**
     * 英语博客/推荐博客
     */
    public static final String GET_BLOG_LIST = "/speakEnglish-ums/app-api/v1/blog/getBlogList";

    /**
     * 场景连播
     */
    public static final String GET_SCENE_PLAY_LIST = "/speakEnglish-ums/app-api/v1/project/scenePlaylPage";

    /**
     * 节目精选
     */
    public static final String GET_FEATURED_LIST = "/speakEnglish-ums/app-api/v1/blog/getFeaturedList";

    /**
     * 热门单集（根据收藏数和点赞数进行排序）
     */
    public static final String GET_HOT_BLOG_LIST = "/speakEnglish-ums/app-api/v1/blog/getHotBlogList";

    /**
     * 根据分类ID获取分类场景信息
     */
    public static final String GET_SCENE_PLAY_DATA_LIST = "/speakEnglish-ums/app-api/v1/project/scenePlayDatalistPage";

    /**
     * 节目精选(博客详细信息)
     */
    public static final String GET_BLOG_DETAIL = "/speakEnglish-ums/app-api/v1/blog/findOneFeatured";

    /**
     * 根据博客ID获取所有的单集
     */
    public static final String GET_BLOG_LIST_FOR_CATEGORY = "/speakEnglish-ums/app-api/v1/blog/getBlogListForCategory";

    /**
     * 获取开口说详情
     */
    public static final String GET_OPEN_SPEAKER_DETAIL = "/speakEnglish-ums/app-api/v1/project/openSpeak/findOne";
    public static final String GET_OPEN_SPEAKER_LIST_BY_SUB = "/speakEnglish-ums/app-api/v1/project/openSpeak/listBySub";
    /**
     * 精品课程/课程预览
     */
    public static final String COURSE_PREVIEW = "/speakEnglish-ums/app-api/v1/project/priviewHeader";
    /**
     * 词汇热身
     */
    public static final String GET_WORDS = "/speakEnglish-ums/app-api/v1/project/getWords";
    /**
     * 课程精讲详情
     */
    public static final String GET_SCENE_PROJECT_DETAIL = "/speakEnglish-ums/app-api/v1/project/queryScenesProject";

    /**
     * 获取单词评分
     */
    public static final String GET_SCORE = "/speakEnglish-ums/api/v1/files/getScore";

    /**
     * 场景对话详情
     */
    public static final String GET_SCENE_SPEAK_DETAIL = "/speakEnglish-ums/app-api/v1/project/queryScenesSpeakProject";

    /**
     * 博客详情
     */
    public static final String GET_ONE_BLOG_ISSUE = "/speakEnglish-ums/app-api/v1/blog/findOneBlogIssue";

    /**
     * 收藏英语博客
     */
    public static final String ADD_BLOG_FAVORITE = "/speakEnglish-ums/tableFavorites/addBlog";

    /**
     * 移除收藏
     */
    public static final String REMOVE_BLOG_FAVORITE = "/speakEnglish-ums/tableFavorites/remove";

        /**
     * 收藏精品课程
     */
    public static final String ADD_COURSE_CATALOG = "/speakEnglish-ums/tableFavorites/addCourseCatalog";

    /**
     * 收藏场景课程
     */
    public static final String ADD_COURSE_PROJECT = "/speakEnglish-ums/tableFavorites/addProject";

    /**
     * 收藏专项课程
     */
    public static final String ADD_COURSE_SPECIAL = "/speakEnglish-ums/tableFavorites/addSpecialization";

    /**
     * 问题列表
     */
    public static final String GET_QUESTIONS = "/speakEnglish-ums/app-api/v1/project/queryQuestions";

    /**
     * 开口说对话详情
     */
    public static final String GET_SPEAK_CHAT_DETAIL = "/speakEnglish-ums/app-api/v1/project/openSpeak/findScroll";

    /**
     * 获取文本点评和解析
     */
    public static final String GET_CONTENT_ANALYSIS = "/speakEnglish-ums/app-api/v1/openspeak/openSpeak/queryanalysis";

    /**
     * 获取收藏列表
     */
    public static final String GET_FAVORITE_LIST = "/speakEnglish-ums/tableFavorites/queryList";

    /**
     * 收藏场景连播
     */
    public static final String ADD_STREAM_FAVORITE = "/speakEnglish-ums/tableFavorites/addStream";

    /**
     * 添加生词本
     */
    public static final String ADD_NEW_WORD = "/speakEnglish-ums/app-api/v1/user/insertNotebook";

    /**
     * 获取生词本
     */
    public static final String GET_NEW_WORD_LIST = "/speakEnglish-ums/app-api/v1/user/queryNotebook";

    /**
     * 移除生词本
     */
    public static final String REMOVE_NEW_WORD = "/speakEnglish-ums/app-api/v1/user/batchDeleteNotebook";

    /**
     * 获取商品和优惠卷信息
     */
    public static final String GET_VIP_INFO = "/speakEnglish-ums/api/v1/product/vipInfo/0";

    /**
     * 生成订单号
     */
    public static final String PAY_ORDER = "/speakEnglish-ums/api/v1/product/payOrder";

    /**
     * 优惠券列表
     */
    public static final String GET_COUPON_LIST = "/speakEnglish-ums/api/v1/product/coupons";

    /**
     * 笔记列表
     */
    public static final String GET_NOTE_LIST = "/speakEnglish-ums/app-api/v1/noteBook/list";

    /**
     * 更新笔记
     */
    public static final String UPDATE_NOTE_INFO = "/speakEnglish-ums/app-api/v1/updateNoteBook";

    /**
     * 订单列表
     */
    public static final String GET_ORDER_LIST = "/speakEnglish-ums/app-api/v1/order/info";

    /**
     * 课程记录
     */
    public static final String GET_COURSE_RECORD_LIST = "/speakEnglish-ums/app-api/v1/learn/record/project";

    /**
     * 博客记录
     */
    public static final String GET_BLOG_RECORD_LIST = "/speakEnglish-ums/app-api/v1/learn/record/blog";

    /**
     * 等级自测
     */
    public static final String GET_LEVEL_TEST_LIST = "/speakEnglish-ums/app-api/v1/levelTest";

    /**
     * 测试结果
     */
    public static final String QUERY_TEST_RESULT = "/speakEnglish-ums/app-api/v1/queryResult";

    /**
     * 设置当日目标时间
     */
    public static final String SET_TARGET_TIME = "/speakEnglish-ums/app-api/v1/user/setTargetTime";

    /**
     * 课程抵扣卷（是否领取列表）
     */
    public static final String GET_COUPON_RECEIVE_LIST = "/speakEnglish-ums/api/v1/product/coupons/list";

    /**
     * 课程抵扣卷领取
     */
    public static final String GET_COUPON_BY_ID = "/speakEnglish-ums/api/v1/product/coupons/queryId";

    /**对话报告
     *
     */
    public static final String GET_CHAT_REPORT = "/speakEnglish-ums/app-api/v1/openspeak/openSpeak/report";

    /**
     * 对话历史
     */
    public static final String GET_CHAT_HISTORY_LIST = "/speakEnglish-ums/app-api/v1/openspeak/openSpeak/history";
}
