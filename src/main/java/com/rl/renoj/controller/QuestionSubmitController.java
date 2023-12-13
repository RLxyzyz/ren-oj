package com.rl.renoj.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rl.renoj.annotation.AuthCheck;
import com.rl.renoj.common.BaseResponse;
import com.rl.renoj.common.ErrorCode;
import com.rl.renoj.common.ResultUtils;
import com.rl.renoj.constant.UserConstant;
import com.rl.renoj.exception.BusinessException;
import com.rl.renoj.exception.ThrowUtils;
import com.rl.renoj.model.dto.question.QuestionQueryRequest;
import com.rl.renoj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.rl.renoj.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.rl.renoj.model.entity.Question;
import com.rl.renoj.model.entity.QuestionSubmit;
import com.rl.renoj.model.entity.User;
import com.rl.renoj.model.enums.UserRoleEnum;
import com.rl.renoj.model.vo.QuestionSubmitVO;
import com.rl.renoj.model.vo.QuestionVO;
import com.rl.renoj.service.QuestionSubmitService;
import com.rl.renoj.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 题目提交接口
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@RestController
@RequestMapping("/question_submit")
@Slf4j
public class QuestionSubmitController {

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Resource
    private UserService userService;

    /**
     * 点赞 / 取消点赞
     *
     * @param questionSubmitAddRequest
     * @param request
     * @return resultNum 本次点赞变化数
     */
    @PostMapping("/")
    public BaseResponse<Long> doQuestionSubmit(@RequestBody QuestionSubmitAddRequest questionSubmitAddRequest,
                                         HttpServletRequest request) {
        if (questionSubmitAddRequest == null || questionSubmitAddRequest.getQuestionId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能点赞
        final User loginUser = userService.getLoginUser(request);
        long questionId = questionSubmitAddRequest.getQuestionId();
        long result = questionSubmitService.doQuestionSubmit(questionSubmitAddRequest, loginUser);
        return ResultUtils.success(result);
    }
    /**
     * 分页获取当前用户创建的资源列表
     *
     * @param questionQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<QuestionSubmitVO>> listQuestionSubmitByPage(@RequestBody QuestionSubmitQueryRequest questionQueryRequest,
                                                                   HttpServletRequest request) {
        long current=questionQueryRequest.getCurrent();
        long pageSize = questionQueryRequest.getPageSize();
        User loginUser = userService.getLoginUser(request);
        Page<QuestionSubmit> questionSubmitPage = questionSubmitService.page(new Page<>(current, pageSize),
                questionSubmitService.getQueryWrapper(questionQueryRequest));
        return ResultUtils.success(questionSubmitService.getQuestionSubmitVOPage(questionSubmitPage,loginUser));
    }

}
