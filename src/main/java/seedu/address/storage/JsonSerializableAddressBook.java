package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.address.MainApp;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Person;

/**
 * An Immutable AddressBook that is serializable to JSON format.
 */
@JsonRootName(value = "addressbook")
class JsonSerializableAddressBook {

    public static final String MESSAGE_DUPLICATE_PERSON = "Persons list contains duplicate person(s).";

    private static final Logger logger = LogsCenter.getLogger(MainApp.class);

    private final List<JsonAdaptedPerson> persons = new ArrayList<>();

    /**
     * Constructs a {@code JsonSerializableAddressBook} with the given persons.
     */
    @JsonCreator
    public JsonSerializableAddressBook(@JsonProperty("persons") List<JsonAdaptedPerson> persons) {
        this.persons.addAll(persons);
    }

    /**
     * Converts a given {@code ReadOnlyAddressBook} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableAddressBook}.
     */
    public JsonSerializableAddressBook(ReadOnlyAddressBook source) {
        persons.addAll(
                source.getPersonList().stream().map(JsonAdaptedPerson::new).collect(Collectors.toList()));
    }

    /**
     * Converts this address book into the model's {@code AddressBook} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public AddressBook toModelType() throws IllegalValueException {
        AddressBook addressBook = new AddressBook();
        for (JsonAdaptedPerson jsonAdaptedPerson : persons) {
            addPersonToAddressBook(jsonAdaptedPerson, addressBook);
        }
        return addressBook;
    }

    /**
     * Adds a person to the address book.
     *
     * @param jsonAdaptedPerson
     * @param addressBook
     * @throws IllegalValueException
     */
    private void addPersonToAddressBook(JsonAdaptedPerson jsonAdaptedPerson, AddressBook addressBook)
            throws IllegalValueException {
        Person person = jsonAdaptedPerson.toModelType();
        if (addressBook.hasPerson(person)) {
            logger.warning(MESSAGE_DUPLICATE_PERSON + " The following person contact will be discarded:\n"
                    + "Name: " + person.getName() + "\n" + "Phone: " + person.getPhone() + "\n" + "Email: "
                    + person.getEmail() + "\n" + "Address: " + person.getAddress() + "\n" + "Tags: "
                    + person.getTags());
            return;
        }
        addressBook.addPerson(person);
    }

}
