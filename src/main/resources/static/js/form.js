function test(event){
    console.log(event.target.files[0])
    const inputId = event.target.id;
    const element = document.getElementById("file-" + inputId);
    element.classList.remove("d-none");
    element.setAttribute("src", window.URL.createObjectURL(event.target.files[0]));
    console.log();
}
