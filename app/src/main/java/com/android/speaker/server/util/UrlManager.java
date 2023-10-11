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
    public static final String GET_OPEN_SPEAKER_DETAIL = "/speakEnglish-ums/app-api/v1/project/openSpeakb/findOne";
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
    public static final String ADD_BLOG = "/speakEnglish-ums/tableFavorites/addBlog";

    /**
     * 移除收藏
     */
    public static final String REMOVE_BLOG = "/speakEnglish-ums/tableFavorites/remove";

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
}
