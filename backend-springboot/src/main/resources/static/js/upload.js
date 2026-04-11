const fileInput = document.getElementById("fileInput");
const imagePreview = document.getElementById("imagePreview");

if (fileInput && imagePreview) {
    fileInput.addEventListener("change", () => {
        const [file] = fileInput.files;
        if (!file) {
            imagePreview.style.display = "none";
            imagePreview.removeAttribute("src");
            return;
        }

        const reader = new FileReader();
        reader.onload = (event) => {
            imagePreview.src = event.target?.result;
            imagePreview.style.display = "block";
        };
        reader.readAsDataURL(file);
    });
}
