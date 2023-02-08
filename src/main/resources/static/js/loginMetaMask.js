function truncateAddress(address) {
    if (!address) {
        return "";
    }
    return `${address.substr(0, 5)}...${address.substr(
        address.length - 5,
        address.length
    )}`;
}


// Display or remove the users know address on the frontend
function showAddress() {
    if (!window.userAddress) {
        document.getElementById("userAddress").innerText = "";
        document.getElementById("logoutButton").classList.add("hidden");
        return false;
    }

    window.alert(`ETH Address: ${truncateAddress(window.userAddress)}`);
    //document.getElementById(
    //   "userAddress"
    //).innerText = `ETH Address: ${truncateAddress(window.userAddress)}`;
    //document.getElementById("logoutButton").classList.remove("hidden");
}


/*
// Login with Web3 via Metamasks window.ethereum library
async function loginWithEth() {
    if (window.web3) {
        try {
            const selectedAccount = await window.ethereum
                .request({
                    method: "eth_requestAccounts",
                })
                .then((accounts) => accounts[0])
                .catch(() => {
                    throw Error("No account selected!");
                });
            window.userAddress = selectedAccount;
            window.localStorage.setItem("userAddress", selectedAccount);
            document.getElementById("metaInput").value = selectedAccount
        } catch (error) {
            console.error(error);
        }
    } else {
        alert("No ETH brower extension detected.");
    }
}
*/
function getAddress(){
    var address = document.getElementById("metaInput").value

    localStorage.setItem("address", address )
    window.userAddress = address
    return true;
}

async function insertAddressLogout(formId, inputId) {
    var indirizzo = localStorage.getItem("address")
   localStorage.removeItem("address")
    document.getElementById(inputId).value = indirizzo
    document.getElementById(formId).submit()
}
async function insertAddress(formId, inputId){
    if (window.web3) {
        try {
            const selectedAccount = await window.ethereum
                .request({
                    method: "eth_requestAccounts",
                })
                .then((accounts) => accounts[0])
                .catch(() => {
                    throw Error("No account selected!");
                });
            window.userAddress = selectedAccount;
            localStorage.setItem("address", selectedAccount )

            document.getElementById(inputId).value = selectedAccount
            document.getElementById(formId).submit()
        } catch (error) {
            console.error(error);
        }
    } else {
        alert("No ETH brower extension detected.");
    }
}
