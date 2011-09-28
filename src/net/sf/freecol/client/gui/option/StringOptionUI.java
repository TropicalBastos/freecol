/**
 *  Copyright (C) 2002-2011  The FreeCol Team
 *
 *  This file is part of FreeCol.
 *
 *  FreeCol is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  FreeCol is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with FreeCol.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.sf.freecol.client.gui.option;

import java.util.List;
import java.util.logging.Logger;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import net.sf.freecol.client.gui.i18n.Messages;
import net.sf.freecol.client.gui.plaf.FreeColComboBoxRenderer;
import net.sf.freecol.common.option.StringOption;


/**
 * This class provides visualization for an {@link
 * net.sf.freecol.common.option.StringOption}. In order to enable
 * values to be both seen and changed.
 */
public final class StringOptionUI extends OptionUI<StringOption>  {

    private JComboBox box = new JComboBox();

    /**
    * Creates a new <code>StringOptionUI</code> for the given <code>StringOption</code>.
    *
    * @param option The <code>StringOption</code> to make a user interface for
    * @param editable boolean whether user can modify the setting
    */
    public StringOptionUI(final StringOption option, boolean editable) {
        super(option, editable);

        List<String> choices = option.getChoices();

        box.setModel(new DefaultComboBoxModel(choices.toArray(new String[choices.size()])));
        box.setSelectedItem(option.getValue());
        box.setRenderer(new ChoiceRenderer());

        initialize();
    }

    /**
     * {@inheritDoc}
     */
    public JComboBox getComponent() {
        return box;
    }

    /**
     * {@inheritDoc}
     */
    public void updateOption() {
        StringOption option = getOption();
        if (box.getSelectedIndex() == 0 && option.addNone()) {
            option.setValue(StringOption.NONE);
        } else {
            option.setValue((String) box.getSelectedItem());
        }
    }

    /**
     * {@inheritDoc}
     */
    public void reset() {
        StringOption option = getOption();
        if (option.getValue() == null && option.addNone()) {
            box.setSelectedIndex(0);
        } else {
            box.setSelectedItem(option.getValue());
        }
    }

    private class ChoiceRenderer extends FreeColComboBoxRenderer {

        @Override
        public void setLabelValues(JLabel label, Object value) {
            String id = (String) value;
            if ("none".equals(id)) {
                label.setText(Messages.message(id));
            } else {
                label.setText(Messages.message((String) value + ".name"));
            }
        }
    }
}
