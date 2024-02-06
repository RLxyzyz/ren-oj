package com.rl.renoj.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rl.renoj.common.ErrorCode;
import com.rl.renoj.constant.CommonConstant;
import com.rl.renoj.exception.BusinessException;
import com.rl.renoj.judge.JudgeService;
import com.rl.renoj.judge.codesandbox.model.JudgeInfo;
import com.rl.renoj.model.dto.question.QuestionQueryRequest;
import com.rl.renoj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.rl.renoj.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.rl.renoj.model.entity.*;
import com.rl.renoj.model.enums.QuestionSubmitLanguageEnum;
import com.rl.renoj.model.enums.QuestionSubmitStatusEnum;
import com.rl.renoj.model.vo.QuestionSubmitListVO;
import com.rl.renoj.model.vo.QuestionSubmitVO;
import com.rl.renoj.model.vo.QuestionVO;
import com.rl.renoj.model.vo.UserVO;
import com.rl.renoj.service.QuestionService;
import com.rl.renoj.service.QuestionSubmitService;
import com.rl.renoj.mapper.QuestionSubmitMapper;
import com.rl.renoj.service.UserService;
import com.rl.renoj.utils.SqlUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
* @author 任磊
* @description 针对表【question_submit(题目提交表)】的数据库操作Service实现
* @createDate 2023-11-28 15:52:05
*/
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit>
    implements QuestionSubmitService{
    @Resource
    private QuestionService questionService;
    @Resource
    private UserService userService;
    @Resource
    @Lazy
    private JudgeService judgeService;

    /**
     * 点赞
     *
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return
     */
    @Override
    public Long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser) {
        String language = questionSubmitAddRequest.getLanguage();
        QuestionSubmitLanguageEnum enumByValue = QuestionSubmitLanguageEnum.getEnumByValue(language);
        if(enumByValue==null)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"编程语言错误");
        }
        long questionId = questionSubmitAddRequest.getQuestionId();
        // 判断实体是否存在，根据类别获取实体
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        long userId = loginUser.getId();
        QuestionSubmit questionSubmit = new QuestionSubmit();
        questionSubmit.setUserId(userId);
        questionSubmit.setQuestionId(questionId);
        questionSubmit.setCode(questionSubmitAddRequest.getCode());
        questionSubmit.setLanguage(language);
        questionSubmit.setStatus(QuestionSubmitStatusEnum.WAITING.getValue());
        boolean save = this.save(questionSubmit);
        if (!save)
        {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"题目提交失败");
        }
        //异步执行判题服务
        CompletableFuture.runAsync(()->{
            QuestionSubmit questionSubmit1 = judgeService.doSubmit(questionSubmit.getId());
        });
        return questionSubmit.getId();
    }

    /**
     * 获取查询包装类
     *
     * @param questionSubmitQueryRequest
     * @return QueryWrapper
     */
    @Override
    public QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest) {


        QueryWrapper<QuestionSubmit> queryWrapper = new QueryWrapper<>();
        if (questionSubmitQueryRequest == null) {
            return queryWrapper;
        }
        Long questionId = questionSubmitQueryRequest.getQuestionId();
        String language = questionSubmitQueryRequest.getLanguage();
        Integer userId = questionSubmitQueryRequest.getUserId();
        Integer status = questionSubmitQueryRequest.getStatus();
        String sortField = questionSubmitQueryRequest.getSortField();
        String sortOrder = questionSubmitQueryRequest.getSortOrder();
        queryWrapper.like(ObjectUtils.isNotEmpty(questionId), "questionId", questionId);
        queryWrapper.like(StringUtils.isNotBlank(language), "language", language);
        queryWrapper.like(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.like(QuestionSubmitStatusEnum.getEnumByValue(status)!=null, "status", status);
        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }


    @Override
    public QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser) {
        QuestionSubmitVO questionSubmitVO = QuestionSubmitVO.objToVo(questionSubmit);
        long questionSubmitId = questionSubmit.getId();
        //获取提交题目的用户
        Long userId = questionSubmit.getId();
        if (loginUser.getId()!=userId&&!userService.isAdmin(loginUser))
        {
            questionSubmitVO.setCode(null);
        }

        return questionSubmitVO;
    }

    @Override
    public Page<QuestionSubmitListVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser) {
        List<QuestionSubmit> questionSubmitList = questionSubmitPage.getRecords();
        Page<QuestionSubmitListVO> questionSubmitVOPage = new Page<>(questionSubmitPage.getCurrent(), questionSubmitPage.getSize(), questionSubmitPage.getTotal());
        if (CollectionUtils.isEmpty(questionSubmitList)) {
            return questionSubmitVOPage;
        }
        List<QuestionSubmitListVO> questionSubmitVOList=new ArrayList<>();
        for (QuestionSubmit questionSubmit : questionSubmitList) {
            QuestionSubmitListVO questionSubmitListVO = new QuestionSubmitListVO();
            Long questionId = questionSubmit.getQuestionId();
            Question question = questionService.getById(questionId);
            Long userId = questionSubmit.getUserId();
            User user = userService.getById(userId);
            String judgeInfoStr = questionSubmit.getJudgeInfo();
            JudgeInfo judgeInfo = JSONUtil.toBean(judgeInfoStr, JudgeInfo.class);
            Long memory = judgeInfo.getMemory();
            Long time = judgeInfo.getTime();
            String result = judgeInfo.getMessage();
            Date createTime = questionSubmit.getCreateTime();
            //==========================================
            questionSubmitListVO.setQuestionName(question.getTitle());
            questionSubmitListVO.setSubmitTime(createTime);
            questionSubmitListVO.setMemory(memory);
            questionSubmitListVO.setTime(time);
            questionSubmitListVO.setResult(result);
            questionSubmitListVO.setLanguage(questionSubmit.getLanguage());
            questionSubmitListVO.setQuestionId(questionId);
            questionSubmitListVO.setUserName(user.getUserName());
            questionSubmitListVO.setId(questionSubmit.getId());

            questionSubmitVOList.add(questionSubmitListVO);
        }
        //List<QuestionSubmitVO> questionSubmitVOList = questionSubmitList.stream().map(questionSubmit -> getQuestionSubmitVO(questionSubmit, loginUser)).collect(Collectors.toList());

        questionSubmitVOPage.setRecords(questionSubmitVOList);
        return questionSubmitVOPage;
    }

}




