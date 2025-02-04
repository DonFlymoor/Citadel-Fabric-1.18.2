package com.github.alexthe666.citadel.client.gui;

import com.github.alexthe666.citadel.Citadel;
import com.github.alexthe666.citadel.client.rewards.CitadelPatreonRenderer;
import com.github.alexthe666.citadel.server.entity.CitadelEntityData;
import com.github.alexthe666.citadel.server.message.PropertiesMessage;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.fabricators_of_create.porting_lib.util.client.Slider;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.OptionsSubScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

@SuppressWarnings("removal")
@Environment(EnvType.CLIENT)
public class GuiCitadelPatreonConfig extends OptionsSubScreen {

    private Slider distSlider;
    private Slider speedSlider;
    private Slider heightSlider;
    private Button changeButton;
    private float rotateDist;
    private float rotateSpeed;
    private float rotateHeight;
    private String followType;

    public GuiCitadelPatreonConfig(Screen parentScreenIn, Options gameSettingsIn) {
        super(parentScreenIn, gameSettingsIn, Component.translatable("citadel.gui.patreon_customization"));
        CompoundTag tag = CitadelEntityData.getOrCreateCitadelTag(Minecraft.getInstance().player);
        float distance = tag.contains("CitadelRotateDistance") ? tag.getFloat("CitadelRotateDistance") : 2F;
        float speed = tag.contains("CitadelRotateSpeed") ? tag.getFloat("CitadelRotateSpeed") : 1;
        float height = tag.contains("CitadelRotateHeight") ? tag.getFloat("CitadelRotateHeight") : 1F;
        rotateDist = roundTo(distance, 3);
        rotateSpeed = roundTo(speed, 3);
        rotateHeight = roundTo(height, 3);
        followType = tag.contains("CitadelFollowerType") ? tag.getString("CitadelFollowerType") : "citadel";
    }

    private void setSliderValue(int i, float sliderValue) {
        boolean flag = false;
        CompoundTag tag = CitadelEntityData.getOrCreateCitadelTag(Minecraft.getInstance().player);
        if(i == 0){
            rotateDist = roundTo(sliderValue, 3);
            tag.putFloat("CitadelRotateDistance", rotateDist);
            //distSlider.isHovered = false;
        }else if(i == 1){
            rotateSpeed = roundTo(sliderValue, 3);
            tag.putFloat("CitadelRotateSpeed", rotateSpeed);
            //speedSlider.isHovered = false;
        }else{
            rotateHeight = roundTo(sliderValue, 3);
            tag.putFloat("CitadelRotateHeight", rotateHeight);
            //heightSlider.isHovered = false;
        }
        CitadelEntityData.setCitadelTag(Minecraft.getInstance().player, tag);
        Citadel.sendMSGToServer(new PropertiesMessage("CitadelPatreonConfig", tag, Minecraft.getInstance().player.getId()));
    }

    public static float roundTo(float value, int places) {
        return value;
    }

    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        drawCenteredString(matrixStack, this.font, this.title, this.width / 2, 20, 16777215);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    protected void init() {
        super.init();
        int i = this.width / 2;
        int j = this.height / 6;
        this.addRenderableWidget(new Button(i - 100, j+ 120, 200, 20, CommonComponents.GUI_DONE, (p_213079_1_) -> {
            this.minecraft.setScreen(this.lastScreen);
        }));
        this.addRenderableWidget(distSlider = new Slider(i - 150 / 2 - 25, j + 30, 150, 20, Component.translatable("citadel.gui.orbit_dist").append(Component.literal( ": ")), Component.literal( ""), 0.125F, 5F, rotateDist, false, true, btn -> {}){
            @Override
            public void updateSlider() {
                GuiCitadelPatreonConfig.this.setSliderValue(0, (float)getValue());
            }
        });
        this.addRenderableWidget(new Button(i - 150 / 2 + 135, j+ 30, 40, 20,  Component.translatable("citadel.gui.reset"), (p_213079_1_) -> {
            this.setSliderValue(0, 0.4F);
        }));
        this.addRenderableWidget(speedSlider = new Slider(i - 150 / 2 - 25, j + 60, 150, 20, Component.translatable("citadel.gui.orbit_speed").append(Component.literal( ": ")), Component.literal( ""), 0.0F, 5F, rotateSpeed, false, true, (btn) -> {}){
            @Override
            public void updateSlider() {
                GuiCitadelPatreonConfig.this.setSliderValue(1, (float)getValue());
            }
        });
        this.addRenderableWidget(new Button(i - 150 / 2 + 135, j+ 60, 40, 20,  Component.translatable("citadel.gui.reset"), (p_213079_1_) -> {
            this.setSliderValue(1, 1F / 5F);
        }));
        this.addRenderableWidget(heightSlider = new Slider(i - 150 / 2 - 25, j + 90, 150, 20, Component.translatable("citadel.gui.orbit_height").append(Component.literal( ": ")), Component.literal( ""), 0.0F, 2F, rotateHeight, false, true, (btn) -> {}){
            @Override
            public void updateSlider() {
                GuiCitadelPatreonConfig.this.setSliderValue(2, (float)getValue());
            }
        });
        this.addRenderableWidget(new Button(i - 150 / 2 + 135, j+ 90, 40, 20,  Component.translatable("citadel.gui.reset"), (p_213079_1_) -> {
            this.setSliderValue(2, 0.5F);
        }));
        this.addRenderableWidget(changeButton = new Button(i - 100, j, 200, 20, getTypeText(), (p_213079_1_) -> {
            this.followType = CitadelPatreonRenderer.getIdOfNext(followType);
            CompoundTag tag = CitadelEntityData.getOrCreateCitadelTag(Minecraft.getInstance().player);
            if(tag != null){
                tag.putString("CitadelFollowerType", followType);
                CitadelEntityData.setCitadelTag(Minecraft.getInstance().player, tag);
            }
            Citadel.sendMSGToServer(new PropertiesMessage("CitadelPatreonConfig", tag, Minecraft.getInstance().player.getId()));
            changeButton.setMessage(getTypeText());

        }));

    }

    private  Component getTypeText(){
        return Component.translatable("citadel.gui.follower_type").append(Component.translatable("citadel.follower." + followType));
    }
}
