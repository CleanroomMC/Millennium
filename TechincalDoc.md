# Technical Documentation
If you're going to develop mods based on Millennium, here are
some important changes Millennium done to Minecraft:
## FontRenderer => TextRenderer

> Deprecated, this change will move to Blackbox

`net.minecraft.client.gui.FontRenderer fontRenderer` in `net.minecraft.client.Minecraft`
has been replaced by Millennium with extended child class `io.github.cleanroommc.millennium.client.font.TextRenderer`,
which means there are more modern TextRenderer method implementations,
to use it, cast it into Millennium's TextRenderer first.

It is always safe to cast it into Millennium's TextRenderer.