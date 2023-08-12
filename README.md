# AdvancedBooks

Simple plugin helps you to create books

# Commands

### /advancedbooks

* **/advancedbooks create <name\> [title] [author]** - Create a new book
* **/advancedbooks delete <book\>** - Delete selected book
* **/advancedbooks reload** - Reload Books, config and messages

### /openbook

* **/openbook <book\>** - Open selected book

# Permissions

* **advancedbooks.admin** - Permission for /advancedbooks commands
* **advancedbooks.bypass** - if per-book-permission in config.yml is set to true, this permission grant you to bypass
  check when opening the book
* **advancedbooks.open.<book\>** - if per-book-permission in config.yml is set to true, this permission grant you to
  open the specific book

# Configuration (config.yml)

```yaml
# Set this true to enable permission per book (default false).
per-book-permission: false

# Do you want to open a book on join? Set to "" to disable
open-on-join: ""
```

# Language (messages.properties)

```properties
# This plugin uses MiniMessage as the component text format.
# LEGACY TEXT FORMATTING (&) IS NOT SUPPORTED
# Use https://webui.adventure.kyori.net/ to preview parsed components
# You can find full documentation on the format (including normal colors and formatting, rgb, click/hover events, and more)
# here: https://docs.adventure.kyori.net/minimessage/format
# Messages containing the placeholder "<prefix>" will have it replaced with the following string
prefix=<grey>[<#2b2ed9>Advanced<#5d99c2>Books<grey>]
no-permission=<prefix> <#d63e4d>You do not have permission
no-console=<prefix> <#d63e4d>Console cannot run this command
reload=<prefix> <#31e31e>Plugin reloaded!
not-exists=<prefix> <#d63e4d>Book <grey><book> <#d63e4d>not exists!
already-exists=<prefix> <#d63e4d>Book <grey><book> <#d63e4d>already exists!
created-book=<prefix> <#31e31e>Successfully created <grey><book> <#31e31e>Book
deleted-book=<prefix> <#d63e4d>Successfully deleted <grey><book> <#d63e4d>Book
```

[Other translations](https://hangar.papermc.io/LuuckA/AdvancedBooks/pages/Translations)

If you translate to another language, open an issue on [Github](https://github.com/LuuckA21/AdvancedBooks/issues)

# Issue or Support

**Found an issue?** Report the issue on [Github](https://github.com/LuuckA21/AdvancedBooks/issues) or join
the [Discord](https://discord.gg/HQZtzDjzgN)