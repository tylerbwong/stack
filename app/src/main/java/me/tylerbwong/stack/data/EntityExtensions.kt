package me.tylerbwong.stack.data

import me.tylerbwong.stack.data.model.BadgeCounts
import me.tylerbwong.stack.data.model.Question
import me.tylerbwong.stack.data.model.User
import me.tylerbwong.stack.data.persistence.entity.QuestionEntity
import me.tylerbwong.stack.data.persistence.entity.UserEntity

fun Question.toQuestionEntity(sortString: String): QuestionEntity =
    QuestionEntity(
        answerCount = answerCount,
        body = body,
        bodyMarkdown = bodyMarkdown,
        closedDate = closedDate,
        closedReason = closedReason,
        commentCount = commentCount,
        creationDate = creationDate,
        downVoteCount = downVoteCount,
        favoriteCount = favoriteCount,
        isAnswered = isAnswered,
        lastActivityDate = lastActivityDate,
        lastEditDate = lastEditDate,
        lastEditor = lastEditor?.userId,
        owner = owner.userId,
        questionId = questionId,
        score = score,
        shareLink = shareLink,
        tags = tags,
        title = title,
        upVoteCount = upVoteCount,
        viewCount = viewCount,
        sortString = sortString
    )

fun QuestionEntity.toQuestion(owner: UserEntity, lastEditor: UserEntity?): Question =
    Question(
        answerCount = answerCount,
        body = body,
        bodyMarkdown = bodyMarkdown,
        closedDate = closedDate,
        closedReason = closedReason,
        commentCount = commentCount,
        creationDate = creationDate,
        downVoteCount = downVoteCount,
        favoriteCount = favoriteCount,
        isAnswered = isAnswered,
        lastActivityDate = lastActivityDate,
        lastEditDate = lastEditDate,
        lastEditor = lastEditor?.toUser(),
        owner = owner.toUser(),
        questionId = questionId,
        score = score,
        shareLink = shareLink,
        tags = tags,
        title = title,
        upVoteCount = upVoteCount,
        viewCount = viewCount
    )

fun User.toUserEntity(): UserEntity =
    UserEntity(
        aboutMe = aboutMe,
        acceptRate = acceptRate,
        accountId = accountId,
        displayName = displayName,
        link = link,
        location = location,
        profileImage = profileImage,
        reputation = reputation,
        userId = userId,
        userType = userType,
        bronzeBadgeCount = badgeCounts?.bronze ?: 0,
        silverBadgeCount = badgeCounts?.silver ?: 0,
        goldBadgeCount = badgeCounts?.gold ?: 0
    )

fun UserEntity.toUser(): User =
    User(
        aboutMe = aboutMe,
        acceptRate = acceptRate,
        accountId = accountId,
        displayName = displayName,
        link = link,
        location = location,
        profileImage = profileImage,
        reputation = reputation,
        userId = userId,
        userType = userType,
        badgeCounts = BadgeCounts(bronzeBadgeCount, silverBadgeCount, goldBadgeCount)
    )
