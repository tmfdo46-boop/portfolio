// 게시글 불러오기
function loadPosts() {
    $.get("/posts/list", function(posts){
        const postList = $("#postList");
        postList.empty();
        posts.forEach(post=>{
            // 게시글 이미지가 있으면 <img> 추가
            let postImagesHtml = '';
                if (post.imageUrls && post.imageUrls.length > 0) {
                    postImagesHtml = '<div class="post-images-container">';
                    post.imageUrls.forEach(url => {
                        postImagesHtml += `<img src="${url}" alt="게시글 이미지">`;
                    });
                    postImagesHtml += '</div>';
                }

            const followBtnHtml = !post.following && post.userId !== loginUserId
                ? `<button class="follow-btn" data-user-id="${post.userId}" data-nickname="${post.nickname}">+</button>`
                : '';

            const heart = post.likedByMe ? "/icons/like-filled.png" : "/icons/like.png";
            
            const postHtml = `
                <div class="post" data-post-id="${post.id}">
                    <div class="post-header">
                        <img class="profile-img" src="${post.profileImage}">
                        <span class="nickname">${post.nickname}</span>
                        ${followBtnHtml}
                        <span class="created-at">${formatTimeAgo(post.createdAt)}</span>
                    </div>

                    <div class="post-content">
                        <p>${post.content}</p>
                        ${postImagesHtml}
                    </div>

                    <div class="post-footer">
                        <div class="post-action like-btn ${post.likedByMe ? 'liked' : ''}">
                            <img src="${heart}">
                            <span class="like-count" id="like-count-${post.id}">${post.likeCount}</span>
                        </div>
                        <div class="post-action">
                            <img src="/icons/comment.png">
                            <span>${post.commentCount}</span>
                        </div>
                    </div>
                </div>
            `;

            postList.append(postHtml);
        });
    });
    
    updateAlertBadge();
    updateMessageBadge();
}

// 게시글 상세 페이지
function initPostDetail(postId){
    // 게시글 단건
    $.get("/posts/detail/" + postId, function(post){
        let imagesHtml = '';
        if (post.imageUrls && post.imageUrls.length > 0) {
            imagesHtml = '<div class="post-images-container-detail">';
            post.imageUrls.forEach(url => {
                imagesHtml += `<img src="${url}" class="detail-image">`;
            });
            imagesHtml += '</div>';
        }

        const followBtnHtml = !post.following && post.userId !== loginUserId
            ? `<button class="follow-btn" data-user-id="${post.userId}" data-nickname="${post.nickname}">+</button>`
            : '';

        const heart = post.likedByMe ? "/icons/like-filled.png" : "/icons/like.png";

        $("#postContent").html(`
            <div class="post" data-post-id="${post.id}">
                <div class="post-header">
                    <img class="profile-img" src="${post.profileImage}">
                    <span class="nickname">${post.nickname}</span>
                    ${followBtnHtml}
                    <span class="created-at">${formatTimeAgo(post.createdAt)}</span>
                </div>

                <div class="post-content">
                    <p>${post.content}</p>
                    ${imagesHtml}
                </div>

                <div class="post-footer">
                    <div class="post-action like-btn ${post.likedByMe ? 'liked' : ''}">
                        <img src="${heart}">
                        <span class="like-count">${post.likeCount}</span>
                    </div>
                    <div class="post-action">
                        <img src="/icons/comment.png">
                        <span>${post.commentCount}</span>
                    </div>
                </div>
            </div>
        `);
    });

    // 댓글 로드
    function loadComments(){
        $.get(`/comments/${postId}`, function(comments){
            const list = $("#commentList");
            list.empty();
            comments.forEach(c=>{
                list.append(`<div class="comment"><b>${c.authorName}</b>: ${c.content}</div>`);
            });
        });
    }
    loadComments();

    // 댓글 작성
    $("#commentBtn").off("click").on("click", function(){
        const content = $("#commentContent").val();
        if(!content) return;

        $.ajax({
            type: "POST",
            url: "/comments/saveComment",
            contentType: "application/json",
            data: JSON.stringify({ postId: postId, content: content }),
            success: function(){
                $("#commentContent").val("");
                loadComments();
            }
        });
    });
}

// 게시글 작성 화면 이벤트
// 파일 선택 버튼 클릭
$(document).on("click", "#fileIcon", function () {
    $("#postImage").click();
});

// 선택한 이미지 미리보기
$(document).on("change", "#postImage", function() {
    const files = this.files;

    // 미리보기 초기화
    $("#previewContainer").empty();

    // 새로 선택한 파일만 처리
    if (files.length > 0) {
        Array.from(files).forEach(file => {
            const reader = new FileReader();
            reader.onload = function(e) {
                const img = $(`<img src="${e.target.result}" class="preview-img" style="width:100px; margin:5px;">`);
                $("#previewContainer").append(img);
            }
            reader.readAsDataURL(file);
        });
    }
});

// 게시 버튼 클릭
$(document).on("click", "#postSubmitBtn", function() {
    const content = $("#postContent").val();
    const files = $("#postImage")[0].files;

    const formData = new FormData();
    formData.append("content", content);

    Array.from(files).forEach(file => {
        formData.append("images", file);
    });

    $.ajax({
        type: "POST",
        url: "/posts/write",
        data: formData,
        processData: false,
        contentType: false,
        success: function() {
            showToast("작성 완료되었습니다.", "success");
            setTimeout(() => {
                $("#postContent").val("");
                $("#postImage").val("");
                $("#previewContainer").empty();
                window.location.href = "/main";
            }, 2000);
        },
        error: function() {
            showToast("작성 실패하였습니다.", "error");
        }
    });
});

// 좋아요 버튼 클릭 이벤트
$(document).on("click", ".like-btn", function(e) {
    e.stopPropagation();

    const postDiv = $(this).closest(".post");
    const postId = postDiv.data("post-id");
    const likeImg = $(this).find("img");
    
    $.ajax({
        type: "POST",
        url: `/likes/post/${postId}`,
        success: function(postDto) {
            if(postDto.result === "already") {
                $(`#like-count-${postId}`).text(postDto.likeCount);
                $(postDiv).find(".like-btn").removeClass("liked");
                likeImg.attr("src", "/icons/like.png"); // 빈 하트
            }
            else {
                $(`#like-count-${postId}`).text(postDto.likeCount);
                $(postDiv).find(".like-btn").addClass("liked");
                likeImg.attr("src", "/icons/like-filled.png"); // 기본 하트
            }
        },
        error: function() {
            showToast("좋아요 실패", "error");
        }
    });
});

// 팔로우 버튼 클릭
$(document).on("click", ".follow-btn", function (e) {
    e.stopPropagation();
    
    const userId = $(this).data("user-id");
    const userName = $(this).data("nickname");
    const that = $(this);

    $.post(`/follows/status/${userId}`, function () {
        that.text("✔");
        that.prop("disabled", true);
        showToast(userName + " 팔로우 성공", "success");
    });
});

// 게시글 이미지 클릭 시 → 모달
$(document).on("click", ".post-content img", function(e) {
    $("#bottomSheet").removeClass("show");

    e.stopPropagation(); // 이벤트 버블링 방지 → 부모 .post 클릭 방지

    const src = $(this).attr("src");
    $("#modalImage").attr("src", src);
    $("#imageModal").fadeIn();
    $("#modalOptions").hide(); // 옵션 초기화
});

// 모달 닫기
$(document).on("click", "#closeImage", function() {
    $("#imageModal").hide();
});

// ... 버튼 클릭 시 옵션 토글
$(document).on("click", "#moreBtn", function(e) {
    $("#bottomSheet").toggleClass("show");
});

// 갤러리 저장
$(document).on("click", "#saveBtn", function(e) {
    const imageUrl = $("#modalImage").attr("src");
    $("#folderModal").data("imageUrl", imageUrl);
    
    // 사용자 폴더 불러오기
    $.ajax({
        type: "GET",
        url: "/gallery/folders",
        success: function(folders) {
            const folderList = $("#folderList");
            folderList.empty();
            if (folders.length > 0) {
                folders.forEach(folder => {
                    folderList.append(`<button class="folder-btn" id="folder-btn" data-id="${folder.id}">${folder.folderName}</button>`);
                });
                $("#folderModal").show();
            }else{
                showToast("폴더가 없습니다. 새 폴더를 만드세요.", "alert");
                $("#addFolderModal").show();
            }
        }
    });
});

// 폴더 선택
$(document).on("click", "#folder-btn", function(e) {
    $(".folder-btn").removeClass("active");
    $(this).addClass("active");

    const folderId = $(this).data("id");
    saveFolder(folderId);
});

// 해당 폴더에 이미지 저장
function saveFolder(folderId){
    imageUrl = $("#folderModal").data("imageUrl");
    if (!imageUrl) {
        alert("저장할 이미지가 없습니다.");
        return;
    }

    $.ajax({
        type: "POST",
        url: "/gallery/save",
        contentType: "application/json",
        data: JSON.stringify({
            imageUrl: imageUrl,
            folderId: folderId
        }),
        success: function (data) {
            if (data == "folderAlready"){
                showToast("존재하지 않는 폴더입니다.", "error");
            }else if (data == "Already"){
                showToast("이미 해당 폴더에 저장된 이미지입니다.", "error");
            }else{
                $("#folderModal").hide();
                $("#imageModal").hide();

                showToast("갤러리에 저장 완료", "success");
            }
        },
        error: function () {
            showToast("갤러리에 저장 실패","error");
        }
    });
}

// 모달 닫기
$(document).on("click", "#closeFolder", function() {
    $("#folderModal").hide();
});

// 공유하기
$(document).on("click", "#shareBtn", function(e) {
    const url = $("#modalImage").attr("src");
    if (navigator.share) {
        navigator.share({ url: url }).catch(console.error);
    } else {
        showToast("공유 기능이 지원되지 않는 브라우저입니다.", "error");
    }
});
