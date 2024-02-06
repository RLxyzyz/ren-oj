package com.rl.renoj.service.impl;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.rl.renoj.common.ErrorCode;
import com.rl.renoj.constant.CommonConstant;
import com.rl.renoj.exception.BusinessException;
import com.rl.renoj.exception.ThrowUtils;
import com.rl.renoj.model.dto.question.QuestionQueryRequest;
import com.rl.renoj.model.entity.*;
import com.rl.renoj.model.vo.QuestionVO;
import com.rl.renoj.model.vo.UserVO;
import com.rl.renoj.service.QuestionService;
import com.rl.renoj.mapper.QuestionMapper;
import com.rl.renoj.service.UserService;
import com.rl.renoj.utils.SqlUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
* @author 任磊
* @description 针对表【question(题目)】的数据库操作Service实现
* @createDate 2023-11-28 15:51:35
*/
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question>
    implements QuestionService{

    private final static Gson GSON = new Gson();

    @Resource
    private UserService userService;

    /*
    * 校验题目是否合法*/
    @Override
    public void validQuestion(Question question, boolean add) {
       if (question==null)
       {
           throw new BusinessException(ErrorCode.PARAMS_ERROR);
       }
       //创建题目的时候，参数不能为空
        if (add)
        {
            ThrowUtils.throwIf(StringUtils.isAnyBlank(question.getContent(),question.getTitle(),question.getTags()),ErrorCode.PARAMS_ERROR);
        }
        //有参数时进行参数的校验
        if (StringUtils.isNotBlank(question.getTitle())&&question.getTitle().length()>80)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"标题过长");
        }
        if (StringUtils.isNotBlank(question.getContent())&&question.getContent().length()>8192)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"题目内容过长");
        }
        if (StringUtils.isNotBlank(question.getAnswer())&&question.getAnswer().length()>8192)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"题目答案过长");
        }
        if (StringUtils.isNotBlank(question.getJudgeConfig())&& question.getJudgeConfig().length()>8192)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"判题配置过长");
        }
        if (StringUtils.isNotBlank(question.getJudgeCase())&&question.getJudgeCase().length()>8192)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"输入输出用例过长");
        }
    }

    /**
     * 获取查询包装类
     *
     * @param questionQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest) {


        QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
        if (questionQueryRequest == null) {
            return queryWrapper;
        }
        Long id = questionQueryRequest.getId();
        String title = questionQueryRequest.getTitle();
        String content = questionQueryRequest.getContent();
        List<String> tags = questionQueryRequest.getTags();
        String answer = questionQueryRequest.getAnswer();
        Long userId = questionQueryRequest.getUserId();
        String sortField = questionQueryRequest.getSortField();
        String sortOrder = questionQueryRequest.getSortOrder();
        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
        queryWrapper.like(StringUtils.isNotBlank(content), "answer", answer);
        if (CollectionUtils.isNotEmpty(tags)) {
            for (String tag : tags) {
                queryWrapper.like("tags", "\"" + tag + "\"");
            }
        }
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }


    @Override
    public QuestionVO getQuestionVO(Question question, HttpServletRequest request) {
        QuestionVO questionVO = QuestionVO.objToVo(question);
        long questionId = question.getId();
        // 1. 关联查询用户信息
        Long userId = question.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        questionVO.setUserVO(userVO);

        return questionVO;
    }

    @Override
    public Page<QuestionVO> getQuestionVOPage(Page<Question> questionPage, HttpServletRequest request) {
        List<Question> questionList = questionPage.getRecords();
        Page<QuestionVO> questionVOPage = new Page<>(questionPage.getCurrent(), questionPage.getSize(), questionPage.getTotal());
        if (CollectionUtils.isEmpty(questionList)) {
            return questionVOPage;
        }
        List<QuestionVO> questionVOList=new ArrayList<>();
        for (Question question : questionList) {
            QuestionVO questionVO = QuestionVO.objToVo(question);
            questionVOList.add(questionVO);
            System.out.println(questionVO.getJudgeCase());
        }
        questionVOPage.setRecords(questionVOList);
        return questionVOPage;
    }

}




