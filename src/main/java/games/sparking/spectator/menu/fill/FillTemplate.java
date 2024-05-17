package games.sparking.spectator.menu.fill;

import games.sparking.spectator.menu.fill.impl.BorderFiller;
import games.sparking.spectator.menu.fill.impl.FillFiller;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum FillTemplate {

    FILL(new FillFiller()),
    BORDER(new BorderFiller());

    private final IMenuFiller menuFiller;

}
