async function send_data() {
    const url = "/vault/unlock";
    const password = document.getElementById("auth-password").value;
    const csrfToken = document.querySelector('meta[name="_csrf"]').content;
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;

    const res = await fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            [csrfHeader]: csrfToken
        },
        body: JSON.stringify({password: password})
    });
    const data = await res.json();
    if (data === true) {
        alert("vault unlocked successfully!,you may proceed.");
        hideUnlockModal();
    }
    else alert("incorrect login credentials");
}

function showUnlockModal(e){
    if(e)e.stopPropagation();
    document.body.classList.add("blur-body");
    document.getElementById("verificationOverlay").style.display="block";

}
function hideUnlockModal(){
    document.getElementById("verificationOverlay").style.display="none";
    document.body.classList.remove("blur-body");
}
document.addEventListener("click", function(e) {
    const verificationOverlay = document.getElementById("verificationOverlay");
    if (verificationOverlay.style.display === "block") {
        if (!verificationOverlay.contains(e.target)) {
            verificationOverlay.style.display = "none";
            document.body.classList.remove("blur-body");
        }
    }
});