/**
 *  Copyright (C) 2002-2016   The FreeCol Team
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

package net.sf.freecol.common.networking;

import net.sf.freecol.common.model.Game;
import net.sf.freecol.common.model.Player;
import net.sf.freecol.server.FreeColServer;
import net.sf.freecol.server.control.FreeColServerHolder;
import net.sf.freecol.server.model.ServerPlayer;

import org.w3c.dom.Element;


/**
 * A network request handler for the current player will automatically
 * return an error (&quot;not your turn&quot;) if called by a
 * connection other than that of the currently active player. If no
 * game is active or if the player is unknown the same error is
 * returned.
 */
public abstract class CurrentPlayerNetworkRequestHandler
    extends FreeColServerHolder implements NetworkRequestHandler {


    /**
     * Create a new current player request handler.
     *
     * @param freeColServer The enclosing {@code FreeColServer}.
     */
    public CurrentPlayerNetworkRequestHandler(FreeColServer freeColServer) {
        super(freeColServer);
    }


    /**
     * Check if a player is the current player.
     * 
     * @param player The {@code Player} to check.
     * @return true if a game is active and the player is the current one.
     */
    private boolean isCurrentPlayer(Player player) {
        Game game = getGame();
        return (player == null || game == null) ? false
            : player.equals(game.getCurrentPlayer());
    }


    // Override NetworkRequestHandler

    /**
     * {@inheritDoc}
     */
    @Override
    public final Element handle(Connection conn, Element element) {
        final ServerPlayer serverPlayer = getFreeColServer().getPlayer(conn);
        if (!isCurrentPlayer(serverPlayer)) {
            return serverPlayer.clientError("Received message: "
                + element.getTagName() + " out of turn from player: "
                + serverPlayer.getNation())
                .build(serverPlayer);
        }
        return handle(serverPlayer, element);
    }

    
    /**
     * Handle a request for the current player.
     * 
     * @param player The requesting {@code ServerPlayer}.
     * @param element The {@code Element} with the request.
     * @return An answerering {@code Element}, which may be null.
     */
    protected abstract Element handle(ServerPlayer serverPlayer,
                                      Element element);
}
