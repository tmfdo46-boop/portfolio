// 폴더 선택
$(document).on("click", "#folder-list", function(e) {
    $(".folder-list").removeClass("active");
    $(this).addClass("active");

    const folderId = $(this).data("id");
    loadFolderImages(folderId);
});

// 폴더 이미지 불러오기
function loadFolderImages(folderId) {
    $.ajax({
        type: "GET",
        url: `/gallery/list/${folderId}`,
        success: function(images) {
            const gallery = $("#galleryImages");
            gallery.empty();
            images.forEach(img => {
                gallery.append(`<img src="${img.imageUrl}" alt="gallery image">`);
            });
        }
    });
}

// 폴더 생성 모달 열기
$(document).on("click", "#addFolderBtn", function() {
    $("#addFolderModal").show();
});

// 모달 닫기
$(document).on("click", "#closeFolderModal", function() {
    $("#addFolderModal").hide();
});

// 폴더 생성 버튼
$(document).on("click", "#createFolderBtn", function() {
    const folderName = $("#newFolderName").val();
    if(!folderName) { showToast("폴더 이름을 입력해주세요.", "notice"); return; }

    $.ajax({
        type: "POST",
        url: "/gallery/folder/create",
        contentType: "application/json",
        data: JSON.stringify({ folderName: folderName }),
        success: function() {
            $("#addFolderModal").hide();
            loadGalleryFolder();
        },
        error: function(xhr) {
            showToast(xhr.responseText || "폴더 생성 실패", "error");
        }
    });
});