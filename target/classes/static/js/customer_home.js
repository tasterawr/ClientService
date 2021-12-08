function editParameter(paramName){
    document.getElementById(paramName +"-input").classList.toggle('hidden');
    document.getElementById("update-" + paramName +"-btn").classList.toggle('hidden');
}

document.getElementById("edit-name-icon").addEventListener("click", function() {
    editParameter("name")
});

document.getElementById("edit-lastname-icon").addEventListener("click", function() {
    editParameter("lastname")
});

document.getElementById("edit-phone-icon").addEventListener("click", function() {
    editParameter("phone")
});

document.getElementById("edit-email-icon").addEventListener("click", function() {
    editParameter("email")
});

document.getElementById("edit-username-icon").addEventListener("click", function() {
    editParameter("username")
});