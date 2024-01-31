function wordContent(obj) {
    var currleg = $(obj).val().length;
    var length = $(obj).attr('maxlength');
    if (parseInt(currleg) === parseInt(length)) {
        $('.error_content').text('字数请在' + length + '字以内');
    } else {
        $('.error_content').text('');
    }
}

