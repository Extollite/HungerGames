package pl.extollite.hungergames.form;

import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.window.FormWindowSimple;

public class KitWindow extends FormWindowSimple {
    public KitWindow(String title, String content){
        super(title, content);
        this.addButton(new ElementButton("Accept"));
        this.addButton(new ElementButton("Back"));
    }
}
