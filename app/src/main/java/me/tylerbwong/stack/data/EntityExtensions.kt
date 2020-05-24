package me.tylerbwong.stack.data

import me.tylerbwong.stack.data.model.AnswerDraft
import me.tylerbwong.stack.data.model.BadgeCounts
import me.tylerbwong.stack.data.model.Question
import me.tylerbwong.stack.data.model.SearchPayload
import me.tylerbwong.stack.data.model.User
import me.tylerbwong.stack.data.persistence.entity.AnswerDraftEntity
import me.tylerbwong.stack.data.persistence.entity.QuestionEntity
import me.tylerbwong.stack.data.persistence.entity.SearchEntity
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
        downvoted = isDownVoted,
        favoriteCount = favoriteCount,
        favorited = isFavorited,
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
        upvoted = isUpVoted,
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
        isDownVoted = downvoted,
        favoriteCount = favoriteCount,
        isFavorited = favorited,
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
        isUpVoted = upvoted,
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

fun AnswerDraftEntity.toAnswerDraft(): AnswerDraft =
    AnswerDraft(
        questionId = questionId,
        questionTitle = questionTitle,
        updatedDate = updatedDate,
        bodyMarkdown = bodyMarkdown
    )

fun SearchEntity.toSearchPayload(): SearchPayload =
    SearchPayload(
        query = query,
        isAccepted = isAccepted,
        minNumAnswers = minNumAnswers,
        bodyContains = bodyContains,
        isClosed = isClosed,
        tags = tags?.split(";"),
        titleContains = titleContains
    )
