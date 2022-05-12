var products = document.getElementsByClassName("products-block");
var categorySelect = document.getElementById("select-by-category");
var paidTypeSelect = document.getElementById("select-by-paidtype");
var minPrice = document.getElementById("min-price-input");
var maxPrice = document.getElementById("max-price-input");
var categoryFields = document.getElementsByClassName("category-value");
var priceFields = document.getElementsByClassName("price-value");
var paidTypeFields = document.getElementsByClassName("paidtype-value");

function reset(){
    for (let i=0; i<products.length;i++){
        products[i].classList.add('hidden');
    }
}

function filterByCategory(){
    if (categorySelect.value === "Все"){
        for (let i = 0; i < products.length; i++) {
            products[i].classList.remove('hidden');
        }
        return;
    }

    for (let i=0; i<categoryFields.length;i++){
        if (categoryFields[i].innerText === categorySelect.value){
            let parent = categoryFields[i].closest('.products-block');
            parent.classList.remove('hidden');
        }
    }
}

function filterByPaidType(){
    if (paidTypeSelect.value === "Все"){
        return;
    }

    for (let i=0; i<paidTypeFields.length;i++){
        if (paidTypeFields[i].innerText !== paidTypeSelect.value){
            let parent = paidTypeFields[i].closest('.products-block');
            parent.classList.add('hidden');
        }
    }
}

function filterByPrice(){
    if (minPrice.value === "" || maxPrice.value === "")
        return;

    for (let i=0; i<priceFields.length;i++){
        let value = parseFloat(priceFields[i].innerText);
        if (value < parseFloat(minPrice.value) || value > parseFloat(maxPrice.value)){
            let parent = priceFields[i].closest('.products-block');
            parent.classList.add('hidden');
        }
    }
}

document.getElementById("apply-filters-btn").addEventListener("click", function (){
    reset();
    filterByCategory();
    filterByPaidType();
    filterByPrice();
});