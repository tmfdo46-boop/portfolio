$(document).on("click", "#saveProfileBtn", function() {

    const data = {
        nickname: $("#nickname").val(),
        hp: $("#hp").val(),
        address: $("#address").val(),
        bio: $("#bio").val()
    };

    $.ajax({
        type: "PUT",
        url: "/users/profile/update",
        contentType: "application/json",
        data: JSON.stringify(data),
        success: function() {
            showToast("프로필 수정 완료", "success");
            $("#content").load("/users/profilePage", function() {
                loadProfile();
            });
        },
        error: function() {
            showToast("수정 실패", "error");
        }
    });
});

$(document).on("click", "#cancelBtn", function() {
    $("#content").load("/users/profile");
});

$(document).on("input", "#bio", function() {
    this.style.height = "auto";
    this.style.height = this.scrollHeight + "px";
    $("#bioCount").text($(this).val().length);
});
