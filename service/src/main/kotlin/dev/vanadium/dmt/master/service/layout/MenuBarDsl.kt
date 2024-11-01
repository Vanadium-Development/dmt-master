package dev.vanadium.dmt.master.service.layout

import dev.vanadium.dmt.master.commons.rbac.UserRole

data class MenuBar(val categories: MutableList<Category> = mutableListOf()) {
    fun category(name: String, init: Category.() -> Unit) {
        val category = Category(name).apply(init)
        categories.add(category)
    }
}

data class Category(val name: String, val pages: MutableList<Page> = mutableListOf()) {
    fun page(name: String, url: String, icon: String, roles: List<UserRole>, init: Page.() -> Unit = {}) {
        val page = Page(name, url, icon, roles).apply(init)
        pages.add(page)
    }
}

data class Page(val name: String, val url: String, val icon: String, val roles: List<UserRole>)

fun menuBar(init: MenuBar.() -> Unit): MenuBar {
    return MenuBar().apply(init)
}
