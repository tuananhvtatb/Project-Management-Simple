
const BASE_URL = 'http://localhost:6262/api/company';

// load header ở các trang khác nhau
$(function () {
    $("#header").load("header.html");
});


// create 1 element to html
const createCompanyElement = item => {
    var listItem = document.createElement('tr');
    var listCode = document.createElement('td');
    listCode.innerText = item.code;
    var listName = document.createElement('td');
    listName.innerText = item.name;
    var listEmail = document.createElement('td');
    listEmail.innerText = item.email;
    var listPhone = document.createElement('td');
    listPhone.innerText = item.phoneNumber;

    var listStatus = document.createElement('td');
    if (item.status == "STOPPED") {
        listStatus.setAttribute('class', 'status status-stopped');
    } else if (item.status == "WORKING") {
        listStatus.setAttribute('class', 'status status-working');
    } else {
        listStatus.setAttribute('class', 'status status-pending');
    }
    var p = document.createElement('p');
    p.innerText = item.status;
    listStatus.appendChild(p);

    var listButton = document.createElement('td');
    listButton.setAttribute('class', 'performx');
    var button1 = document.createElement('button');
    button1.setAttribute('class', 'detail');
    button1.setAttribute('onclick', 'getDetail("' + item.id + '")')
    button1.innerText = 'Detail';
    var button2 = document.createElement('button');
    button2.setAttribute('class', 'delete');
    button2.setAttribute('onclick', 'deleteCompany("' + item.id + '")')
    button2.innerText = 'Delete';
    listButton.appendChild(button1);
    listButton.appendChild(button2);

    listItem.appendChild(listCode);
    listItem.appendChild(listName);
    listItem.appendChild(listEmail);
    listItem.appendChild(listPhone);
    listItem.appendChild(listStatus);
    listItem.appendChild(listButton);

    return listItem;
};

const updateCompanyList = data => {
    const companyList = document.querySelector('tbody');

    if (Array.isArray(data.content) && data.content.length > 0) {
        data.content.map(companyItem => {
            companyList.appendChild(createCompanyElement(companyItem));
        });
    }
    // else if (companyItems) {
    // //     companyList.appendChild(createCompanyElement(companyItems));
    // // }
    if (data.content.length == 0) {
        alert("Không có company nào!");
        removePage();
    }
};


var getPage = async (id) => {

    var pageSize = document.getElementById("row-page").value;

    let payload = { pageSize: pageSize, pageIndex: id };

    let dataApi = await searchAPI(payload);
    let data = dataApi.data;

    console.log(data);
    if (Number(pageSize) > Number(data.content.length)) {
        document.getElementById('end').innerText = data.content.length + (data.number * data.size);
    } else {
        document.getElementById('end').innerText = (data.number + 1) * data.size;
    }
    document.getElementById('start').innerText = data.number * data.size + 1;
    document.getElementById('total').innerText = id;
    await removeData();
    await updateCompanyList(data);
    await removePage();
    await updatePage(data.totalPages);

    localStorage.setItem("pageCurrent", id);
}

const prePage = async () => {

    let currentPage = localStorage.getItem('pageCurrent');

    if (currentPage > 1) {
        await getPage(Number(currentPage) - 1);
    }
}


const nextPPage = async () => {

    let currentPage = localStorage.getItem('pageCurrent');
    let totalPages = localStorage.getItem('totalPages');

    if (Number(currentPage) < Number(totalPages)) {
        await getPage(Number(currentPage) + 1);
    }
}


const removeData = () => {
    const companyList = document.querySelector('tbody');
    while (companyList.hasChildNodes()) {
        companyList.removeChild(companyList.firstChild);
    }
}

const removePage = () => {
    const pageList = document.querySelector('ul');
    while (pageList.hasChildNodes()) {
        pageList.removeChild(pageList.firstChild);
    }
}

// xóa data cũ và render data mới khi thay đổi số hàng trên 1 trang
document.getElementById("row-page").addEventListener("change", async function () {
    await removeData();
    await removePage();
    await search();
});


const searchAPI = async (payload) => {
    let val = document.getElementById("row-page").value;
    let code = document.getElementById("code").value;
    let name = document.getElementById("name").value;
    let email = document.getElementById("email").value;
    let phone = document.getElementById("phone").value;
    let status = document.getElementById("status").value;

    if (payload == null || payload == "" || payload == undefined) {
        payload = { pageSize: val, pageIndex: 1 };
    }
    // if(code !== null && name != null && email != null && phone !== null && status != null){
    //     removePage();
    //     updatePage()
    //     return;
    // }
    if (code !== "") {
        Object.assign(payload, { textCode: code });

    }
    if (name !== "") {
        Object.assign(payload, { textName: name });
    }
    if (email !== "") {
        Object.assign(payload, { textEmail: email });
    }
    if (phone !== "") {
        Object.assign(payload, { textPhone: phone });
    }
    if (status !== "") {
        Object.assign(payload, { textStatus: status });
    }

    let res = await axios.post(BASE_URL + '/searchByDto', payload)
        .catch(function (error) {
            if (error.response) {
                console.log(error.response.data.code);
                console.log(error.response.data.message);
                console.log(error.response.data.details);
            } else if (error.request) {
                console.log(error.request);
            } else {
                console.log('Error', error.message);
            }
        });
    // document.getElementById('end').innerText = data.totalElements;
    // document.getElementById('total').innerText = payload.pageIndex;
    return res;
}

const search = async () => {
    let dataApi = await searchAPI();
    let data = dataApi.data;
    let pageSize = document.getElementById("row-page").value;
    document.getElementById('start').innerText = 1;
    var end = document.getElementById('end');

    if (Number(data.totalElements) < Number(pageSize)) {
        document.getElementById('end').innerText = data.totalElements;
    } else {
        end.innerText = data.size;
    }

    document.getElementById('start').innerText = data.size * data.number + 1;
    document.getElementById('total').innerText = 1;
    removePage();
    updateCompanyList(data);
    updatePage(data.totalPages);
    localStorage.setItem('pageCurrent', 1);
    //localStorage.setItem('toltalPages',data.totalPages);
};


//search company
document.getElementById("search").addEventListener("click", async function (e) {
    e.preventDefault();
    await removeData();
    await search();
});

// click register
document.getElementById("register").addEventListener("click", async function (e) {
    window.location.href = "http://localhost:52330/layout/register.html";
});

document.getElementById("back").addEventListener("click", async function (e) {
    window.location.reload();
});


// pagination element
const createPageElement = (id) => {
    var li = document.createElement('li');
    li.setAttribute('class', 'page-item');

    var a = document.createElement('a');
    a.innerText = id;
    a.setAttribute('class', 'page-link text-primary');
    a.setAttribute('id', 'link-' + id);
    a.setAttribute('onclick', 'getPage(' + id + ')');

    li.appendChild(a);

    return li;
}
const updatePage = totalPages => {
    const pageList = document.querySelector('ul');

    var next = document.createElement('a');
    next.innerText = 'Next';
    next.setAttribute('class', 'page-link text-primary');
    next.setAttribute('id', 'link-next');
    next.setAttribute('onclick', 'nextPPage()');

    var pre = document.createElement('a');
    pre.innerText = 'Previous';
    pre.setAttribute('class', 'page-link text-primary');
    pre.setAttribute('id', 'link-pre');
    pre.setAttribute('onclick', 'prePage()');

    pageList.appendChild(pre);

    if (totalPages < 5) {
        for (let index = 1; index <= totalPages; index++) {
            pageList.appendChild(createPageElement(index));
        }
    } else {
        let pageNextTo = Number(localStorage.getItem('pageNextTo'));
        var dot = document.createElement('a');
        dot.innerText = '...';
        dot.setAttribute('class', 'page-link text-primary');
        //dot.setAttribute('onclick', 'dotClick('+pageNextTo+')');
        //dot.setAttribute('onclick', 'dotPage(' + pages + ')');

        for (let index = 1; index <= 3; index++) {
            pageList.appendChild(createPageElement(index));
        }
        pageList.appendChild(dot);
        pageList.appendChild(createPageElement(totalPages));

    }
    pageList.appendChild(next);
    localStorage.setItem('totalPages', totalPages);
    
};

const getDetail = (id) => {
    localStorage.setItem('id', id);
    window.location.href = "http://localhost:52330/layout/detail.html";
}

const deleteCompany = async (id) => {
    var x = confirm("Bạn chắc chắn muốn xóa?");

    if (x) {
        let res = await axios.delete(BASE_URL + '/' + id)
            .catch(function (error) {
                if (error.response) {
                    console.log(error.response);
                    console.log(error.response.data.status);
                    console.log(error.response.data.code);
                    console.log(error.response.data.message);
                    console.log(error.response.data.details);
                } else if (error.request) {
                    console.log(error.request);
                } else {
                    console.log('Error', error.message);
                }
            });
        console.log(res);
        if (res) {
            alert('Đã xóa!');
            await removeData();
            await removePage();
            await search();
        }
    } else {
        return;
    }
}

const dotClick = (pageNextTo) => {
    removePage();
    updatePage(pageNextTo);
}
