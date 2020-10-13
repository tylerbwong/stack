package me.tylerbwong.stack.data

import me.tylerbwong.stack.api.model.Answer
import me.tylerbwong.stack.api.model.BadgeCounts
import me.tylerbwong.stack.api.model.Question
import me.tylerbwong.stack.api.model.Site
import me.tylerbwong.stack.api.model.User
import me.tylerbwong.stack.data.model.AnswerDraft
import me.tylerbwong.stack.data.model.QuestionDraft
import me.tylerbwong.stack.data.model.SearchPayload
import me.tylerbwong.stack.data.persistence.entity.AnswerDraftEntity
import me.tylerbwong.stack.data.persistence.entity.AnswerEntity
import me.tylerbwong.stack.data.persistence.entity.QuestionDraftEntity
import me.tylerbwong.stack.data.persistence.entity.QuestionEntity
import me.tylerbwong.stack.data.persistence.entity.SearchEntity
import me.tylerbwong.stack.data.persistence.entity.SiteEntity
import me.tylerbwong.stack.data.persistence.entity.UserEntity
import me.tylerbwong.stack.ui.utils.toHtml
import java.util.Locale

fun Question.toQuestionEntity(): QuestionEntity =
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
        favoriteCount = bookmarkCount,
        favorited = isBookmarked,
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
        viewCount = viewCount
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
        bookmarkCount = favoriteCount,
        isBookmarked = favorited,
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

fun Answer.toAnswerEntity(): AnswerEntity =
    AnswerEntity(
        answerId = answerId,
        isAccepted = isAccepted,
        downVoteCount = downVoteCount,
        upVoteCount = upVoteCount,
        score = score,
        creationDate = creationDate,
        bodyMarkdown = bodyMarkdown,
        questionId = questionId,
        owner = owner.userId,
        lastEditDate = lastEditDate,
        lastEditor = lastEditor?.userId
    )

fun AnswerEntity.toAnswer(owner: UserEntity, lastEditor: UserEntity?): Answer =
    Answer(
        answerId = answerId,
        isAccepted = isAccepted,
        downVoteCount = downVoteCount,
        upVoteCount = upVoteCount,
        score = score,
        creationDate = creationDate,
        bodyMarkdown = bodyMarkdown,
        questionId = questionId,
        owner = owner.toUser(),
        lastEditDate = lastEditDate,
        lastEditor = lastEditor?.toUser()
    )

fun AnswerDraftEntity.toAnswerDraft(): AnswerDraft =
    AnswerDraft(
        questionId = questionId,
        questionTitle = questionTitle.toHtml().toString(),
        updatedDate = updatedDate,
        bodyMarkdown = bodyMarkdown,
        site = site
    )

fun QuestionDraftEntity.toQuestionDraft(): QuestionDraft =
    QuestionDraft(
        id = id,
        title = title,
        updatedDate = updatedDate,
        body = body,
        tags = tags,
        site = site
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

fun SiteEntity.toSite(): Site =
    Site(
        name = name.toHtml().toString(),
        parameter = parameter,
        url = url,
        audience = audience.capitalize(Locale.getDefault()).toHtml().toString(),
        iconUrl = iconUrl
    )

fun Site.toSiteEntity(): SiteEntity =
    SiteEntity(
        name = name,
        parameter = parameter,
        url = url,
        audience = audience,
        iconUrl = iconUrl
    )
