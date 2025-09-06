![modloader: fabric](https://img.shields.io/badge/modloader-fabric-dbd0b4)
![enviroment: server and client](https://img.shields.io/badge/environment-client%20and%20server-b01fe0)
<div style="display: flex; flex-direction: row; justify-content: center; align-items: center; gap: 0.5rem; padding: 1rem;">
    <img src="./src/main/resources/assets/unjank/icon.png" style="image-rendering: pixelated;" height="48px">
    <h1 style="margin: 0; padding: 0;">Unjank</h1>
</div>

> A set of QoL improvements, designed for datapacks.

> This mod depends on [owo-lib](https://modrinth.com/mod/owo-lib)

## Features
### Client-side

> Access the config via `/owo-config unjank` or [Mod Menu](https://modrinth.com/mod/modmenu)


**Disable Focus Border** (default = `True`)

Disables the white outline around text elements in dialogs.

> Disable Focus Border = `False`
![](./assets/screenshots/disabled_focus_border_off.png)

> Disable Focus Border = `True`
![](./assets/screenshots/disabled_focus_border_on.png)

**Disable Warning Box** (default = `True`)

Disables the warning box on custom dialogs.

> Disable Warning Box = `False`
![](./assets/screenshots/disabled_warning_box_off.png)

> Disable Warning Box = `True`
![](./assets/screenshots/disabled_warning_box_on.png)

**Disable Command Warning Screen** (default = `True`)

Disables the warning screen when running commands of op-level 1+.

> Disable Command Warning Screen = `False`
![](./assets/screenshots/disabled_command_warning_off.gif)

> Disable Command Warning Screen = `True`
![](./assets/screenshots/disabled_command_warning_on.gif)


### Server-side

**Gamerule** `sendTriggerFeedback` (default = `disabled`)

Controls the feedback of the `/trigger` command directly.

Possible values:

`disabled`: No command feedback shown to any player.

`sourceOnly`: Command feedback only show to the player that ran it.

`sourceAndOps`: Command feedback shown to the player that ran it *and* operators.
