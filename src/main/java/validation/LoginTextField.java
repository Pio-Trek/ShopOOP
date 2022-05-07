package validation;

import javafx.scene.control.TextField;

/**
 * Force the TextField to input username pattern only.
 */
public class LoginTextField extends TextField {

    @Override
    public void replaceText(int start, int end, String text) {
        if (text.matches("[^-\\s]") || text.isEmpty()) {
            super.replaceText(start, end, text);
        }
    }

    @Override
    public void replaceSelection(String replacement) {
        super.replaceSelection(replacement);
    }
}
