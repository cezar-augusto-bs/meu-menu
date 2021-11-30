package br.com.meumenu.model

class User {
    var name: String = ""
    var password: String = ""
    var email: String = ""

    constructor() {}

    constructor(name: String, password: String, email: String) {
        this.name = name
        this.password = password
        this.email = email
    }
}