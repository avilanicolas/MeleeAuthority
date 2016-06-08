package main.java.meleeauthority;

import java.util.List;
import java.util.Set;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.ImmutableList;

@RestController
public class MeleeCharacterController {

    private static MeleeDB meleeDB = null;
    private static List<String> characterNames = null;
    private static Set<String> filterableFields = null;

    private static final String ALL = "ALL";
    private static final String NONE = "NONE";
    private static final float ZERO = 0;

    @RequestMapping("/character")
    public List<MeleeCharacter> character(
            @RequestParam(value="name", defaultValue=ALL) String name,
            @RequestParam(value="filter", defaultValue=NONE) String filter,
            @RequestParam(value="condition", defaultValue=NONE) String predicate,
            @RequestParam(value="value", defaultValue=NONE) String value) {
        List<MeleeCharacter> list = null;

        if (!filter.equals(NONE)) {
            CharacterPredicate characterPredicate = null;
            Float parsedFloat = null;

            try {
                characterPredicate = CharacterPredicate.valueOf(predicate);
                parsedFloat = Float.parseFloat(value);
                
            } catch (IllegalArgumentException | NullPointerException e) {
                e.printStackTrace();
            }

            if (characterPredicate != null
                && parsedFloat != null
                && validFilter(filter)) {
                list = meleeDB.getFilteredCharacters(filter, characterPredicate, parsedFloat);
                System.out.printf("Resultant size: %d\n", list.size());
                return list;
            } else {
                System.out.printf("REJECTED request with (%s %s %s)\n", filter, predicate, value);
            }
        }


        if (list == null && name.equals(ALL)) {
            list = getDB().getAllCharacters();
        } else {
            if (validName(name)) {
                list = ImmutableList.of(getDB().getCharacter(name));
            } else {
                list = ImmutableList.of();
            }
        }

        return list;
    }

    private boolean validFilter(String filter) {
        return getFilterableFields().contains(filter);
    }

    private boolean validName(String name) {
        return getCharacterNames().contains(name);        
    }

    private Set<String> getFilterableFields() {
        if (filterableFields == null) {
            filterableFields = getDB().getFilterableCharacterFields();
        }

        return filterableFields;
    }

    private List<String> getCharacterNames() {
        if (characterNames == null) {
            characterNames = getDB().getCharacterNames();
        }

        return characterNames;
    }

    private MeleeDB getDB() {
        if (meleeDB == null) {
            meleeDB = Application.getDB();
        }

        return meleeDB;
    }
}
