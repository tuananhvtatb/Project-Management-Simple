
$(function () {
    $("#confirm").on("click", async function (e) {
        e.preventDefault();
        var form = $("#cert-form")[0];
        var isValid = form.checkValidity();
        if (!isValid) {
            e.preventDefault();
            e.stopPropagation();
        } else {
            await addAPI();
        }



        form.classList.add('was-validated');
        //window.location.href = "http://localhost:52330/layout/management.html";
        return false; // For testing only to stay on this page
    });
});

$(function () {
    $("#header").load("header.html");
});

const checkCode = async (id, code) => {
    let res = await axios.get('http://localhost:6262/api/company/checkCode?id=' + id + '&code=' + code);
    let result = res.data;
    return result;
}
var addAPI = async (e) => {
    let code = document.getElementById("code").value;
    let check = await checkCode('', code);

    let name = document.getElementById("name").value;
    let address = document.getElementById("address").value;
    let email = document.getElementById("email").value;
    let phone = document.getElementById("phone").value;
    if (document.getElementById('website') != null) {
        var website = document.getElementById("website").value;
    }
    let status = 'PENDING';

    var payload = { code: code, name: name, address: address, email: email, phoneNumber: phone, status: status }

    if (website !== "" || website !== undefined || website !== null) {
        Object.assign(payload, { website: website });
    }
    if (check) {
        alert("Trùng!");
        return;
    } else {
        let res = await axios.post('http://localhost:6262/api/company', payload)
            .catch(function (error) {
                if (error.response) {
                   
                    console.log(error.response.data);
                    console.log(error.response.status);
                    console.log(error.response.headers);
                } else if (error.request) {
                   
                    console.log(error.request);
                } else {
    
                    console.log('Error', error.message);
                }

            });

        let data = res.data;

        if (data) {
            alert("Thêm thành công!");
        } else {
            alert("Có lỗi xảy ra!");
        }
        window.location.href = "http://localhost:52330/layout/management.html";
    }



}

document.getElementById("back").addEventListener("click", function (e) {
    e.preventDefault();
    e.stopPropagation();
    window.location.href = "http://localhost:52330/layout/management.html";
});
