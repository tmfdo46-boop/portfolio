// 탭 클릭 이벤트
$(document).on("click", ".tab", function() {
    const tab = $(this).data("tab");

    $(".tab").removeClass("active");
    $(this).addClass("active");

    $(".tab-panel").removeClass("active");

    if (tab === "post") {
        $("#postPanel").addClass("active");
        loadMyPosts();
    } else if (tab === "gallery") {
        $("#galleryPanel").addClass("active");
    }
});

// 프로필 정보 불러오기
function loadProfile() {
    $.ajax({
        type: "GET",
        url: `/users/profile?userId=${loginUserId}`,
        success: function(user) {
            const userData = user.user;
            $("#nickname").text(userData.nickname);
            // 줄바꿈 문자 통일
            // const bio = user.bio.replace(/\\n|\r/g, "\n");
            // $("#bio").text(bio);
            $("#bio").html(userData.bio ? userData.bio.replace(/\\n/g, "<br>") : "");
            $("#profileImg").attr("src", userData.profileImg || "/images/default_profile.png");
            $("#postCount").text(user.postCount || 0);
            $("#followingCount").text(user.followingCount || 0);
            $("#followerCount").text(user.followerCount || 0);

            if(user.isOwner){
                $(".upload-btn").show();
            } else {
                $(".upload-btn").hide();
            }

            loadMyPosts();
        }
    });
}

// 내 게시글만 불러오기
function loadMyPosts() {
    $.get("/posts/listMy/", function(posts){
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

            const heart = post.likedByMe ? "/icons/like-filled.png" : "/icons/like.png";
            
            const postHtml = `
                <div class="post" data-post-id="${post.id}">
                    <div class="post-header">
                        <img class="profile-img" src="${post.profileImage}">
                        <span class="nickname">${post.nickname}</span>
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
}

// 프로필 사진 업로드
$(document).on("change", "#profileUpload", function() {
    const file = this.files[0];
    if (!file) return;

    const formData = new FormData();
    formData.append("file", file);

    $.ajax({
        url: "/users/profile/image", // 기존 업로드 REST API
        type: "POST",
        data: formData,
        processData: false,
        contentType: false,
        success: function() {
            // 업로드 후 새 이미지 표시
            const reader = new FileReader();
            reader.onload = function(e) {
                $("#profileImg").attr("src", e.target.result);
            }
            reader.readAsDataURL(file);
        },
        error: function() {
            showToast("프로필 사진 업로드 실패", "error");
        }
    });
});

// 프로필 편집 버튼 클릭
$(document).on("click", "#editProfileBtn", function() {
    loadProfileEdit();
});

function loadProfileEdit() {
    $("#content").load("/users/profile/edit", function() {

        $.ajax({
            type: "GET",
            url: `/users/profile?userId=${loginUserId}`,
            success: function(user) {
                const userData = user.user;
                $("#email").val(userData.email);
                $("#name").val(userData.name);
                $("#nickname").val(userData.nickname);
                $("#hp").val(userData.hp);
                $("#address").val(userData.address);
                $("#bio").val(userData.bio);
                $("#bioCount").text(userData.bio ? userData.bio.length : 0);
            }
        });
    });
}

// 공유하기
$(document).on("click", "#shareProfileBtn", function() {
    const userId = loginUserId;
    const profileUrl = window.location.origin + "/users/profile/" + userId;

    // 모바일 / 최신 브라우저
    if (navigator.share) {
        navigator.share({
            title: "내 프로필",
            text: "내 프로필을 확인해보세요",
            url: profileUrl
        }).catch(() => {});
    }  else {// PC / 공유 API 미지원
        copyToClipboard(profileUrl);
        showToast("프로필 링크가 복사되었습니다!", "sucess");
    }
});