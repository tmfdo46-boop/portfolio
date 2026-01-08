$(document).ready(function() {
    
    $("#fileIcon").off("click").click(function() {
        $("#postImage").click();
    });

    // 선택한 이미지 미리보기
    $("#postImage").off("change").on("change", function() {
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
    $("#postSubmitBtn").off("click").on("click", function() {
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
                alert("게시 완료!");
                $("#postContent").val("");
                $("#postImage").val("");
                $("#previewContainer").empty();
                window.location.href = "/main";
            },
            error: function() {
                alert("게시 실패");
            }
        });
    });
});
