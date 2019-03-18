package integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import uk.co.caeldev.builder4test.Builder;
import uk.co.caeldev.builder4test.impl.Pojo;

import java.util.List;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.co.caeldev.builder4test.impl.PojoBuilder.creator;
import static uk.co.caeldev.builder4test.impl.PojoBuilder.creatorWithPredefinedCreatorDefaults;
import static uk.co.caeldev.builder4test.impl.PojoBuilder.creatorWithPredefinedDefaults;
import static uk.co.caeldev.builder4test.impl.PojoBuilder.name;
import static uk.co.caeldev.builder4test.impl.PojoBuilder.name2;
import static uk.co.caeldev.builder4test.impl.PojoBuilder.testValue;
import static uk.co.caeldev.builder4test.impl.PojoBuilder.value;
import static uk.co.caeldev.builder4test.impl.PojoBuilder.value2;
import static uk.co.caeldev.builder4test.impl.PojoBuilder.valueCreator;
import static uk.co.caeldev.builder4test.impl.PojoBuilder.valueTestCreator;
import static uk.org.fyodor.generators.RDG.string;

public class BuilderIntegrationTest {

    @Test
    @DisplayName("should build a pojo successfully using defaults values")
    public void shouldBuildPojo() {
        //When
        Pojo pojo = Builder.build().entity(creator).get();

        //Then
        assertThat(pojo.getName()).isEqualTo("defaultName");
        assertThat(pojo.getValue()).isEqualTo("defaultValue");
    }

    @Test
    @DisplayName("should build a pojo successfully when override default values")
    public void shouldBuildPojoOverridingValues() {
        //When
        Pojo pojo = Builder.build()
                .entity(creator)
                .override(name, "nameoverrideed")
                .override(value, "valueoverrided")
                .get();

        //Then
        assertThat(pojo.getName()).isEqualTo("nameoverrideed");
        assertThat(pojo.getValue()).isEqualTo("valueoverrided");
    }

    @Test
    @DisplayName("should build different pojo successfully when overrides the default values for both objects")
    public void shouldBuilTwoDifferentInstancesWithDifferentValues() {
        //When
        Pojo pojo1 = Builder.build()
                .entity(creator)
                .override(name, "nameoverrideed1")
                .override(value, "valueoverrided1")
                .get();

        //And
        Pojo pojo2 = Builder.build()
                .entity(creator)
                .override(name, "nameoverrideed2")
                .override(value, "valueoverrided2")
                .get();

        //Then
        assertThat(pojo1).isNotEqualTo(pojo2);
        assertThat(pojo1.getName()).isEqualTo("nameoverrideed1");
        assertThat(pojo1.getValue()).isEqualTo("valueoverrided1");
        assertThat(pojo2.getName()).isEqualTo("nameoverrideed2");
        assertThat(pojo2.getValue()).isEqualTo("valueoverrided2");
    }

    @Test
    @DisplayName("should build pojo using defaults values sets by field constructor")
    public void shouldUseDefaultValuesFromFieldInstantiation() {
        //When
        Pojo pojo1 = Builder.build()
                .entity(creatorWithPredefinedDefaults)
                .get();

        //Then
        assertThat(pojo1.getName()).isEqualTo("defaultName");
        assertThat(pojo1.getValue()).isEqualTo("defaultValue");
    }

    @Test
    @DisplayName("should build a pojo successfully overriding defaults values sets by constructor")
    public void shouldOverrideDefaultValuesFromFieldInstantiation() {
        //When
        Pojo pojo1 = Builder.build()
                .entity(creatorWithPredefinedDefaults)
                .override(name2, "overridedName")
                .override(value2, "overridedValue")
                .get();

        //Then
        assertThat(pojo1.getName()).isEqualTo("overridedName");
        assertThat(pojo1.getValue()).isEqualTo("overridedValue");
    }


    @Test
    @DisplayName("should build a pojo successfully using another creators available")
    public void shouldOverrideDefaultValuesFromFieldInstantiationUsingAnotherCreator() {
        //When
        Pojo pojo1 = Builder.build()
                .entity(creatorWithPredefinedDefaults)
                .override(name2, valueCreator)
                .override(value2, "overridedValue")
                .get();

        //Then
        assertThat(pojo1.getName()).isEqualTo("test1");
        assertThat(pojo1.getValue()).isEqualTo("overridedValue");
    }

    @Test
    @DisplayName("should build a pojo by overriding using a creator and overriding a value of it")
    public void shouldOverrideDefaultValuesFromFieldInstantiationUsingAnotherCreator2() {
        //When
        Pojo pojo1 = Builder.build()
                .entity(creatorWithPredefinedCreatorDefaults)
                .override(testValue, "overridedValue1")
                .override(name2, valueTestCreator)
                .get();

        //Then
        assertThat(pojo1.getName()).isEqualTo("overridedValue1");
        assertThat(pojo1.getValue()).isEqualTo("defaultValue");
    }

    @Test
    @DisplayName("should build a pojo successfully setting nulls as values")
    public void shouldOverrideWithNulls() {
        //When
        Pojo pojo = Builder.build()
                .entity(creator)
                .nullify(name)
                .nullify(value)
                .get();

        //Then
        assertThat(pojo.getName()).isNull();
        assertThat(pojo.getValue()).isNull();
    }

    @Nested
    class ListGenerationTest {

        @Test
        @DisplayName("should build a list of two elements overriding defaults values")
        public void shouldBuildAListOfTwoEntities() {

            //When
            List<Pojo> testSiumple = Builder.build()
                    .list(creator)
                    .elements()
                        .element()
                            .override(name, "testSimple")
                            .end()
                        .element()
                            .override(name, "testSimple2")
                            .end()
                    .get();

            //Then
            assertThat(testSiumple).isNotEmpty();
            assertThat(testSiumple).hasSize(2);

            //And
            Pojo pojo = testSiumple.get(0);
            assertThat(pojo.getName()).isEqualTo("testSimple");
            assertThat(pojo.getValue()).isEqualTo("defaultValue");

            //And
            Pojo pojo1 = testSiumple.get(1);
            assertThat(pojo1.getName()).isEqualTo("testSimple2");
            assertThat(pojo1.getValue()).isEqualTo("defaultValue");
        }

        @Test
        @DisplayName("should build a list of two elements overriding defaults values with random generators")
        public void shouldBuildAListOfTwoUsingGenerators() {
            //Given
            int size = 2;

            //When
            Supplier<String> stringSupplier = () -> string().next();

            List<Pojo> testSimple = Builder.build()
                    .list(creator)
                    .size(size)
                    .override(name, stringSupplier)
                    .override(value, stringSupplier)
                    .get();

            //Then
            assertThat(testSimple).isNotEmpty();
            assertThat(testSimple).hasSize(size);

            assertThat(testSimple.get(0).getName()).isNotEqualTo(testSimple.get(1).getName());
            assertThat(testSimple.get(0).getValue()).isNotEqualTo(testSimple.get(1).getValue());
        }

        @Test
        @DisplayName("should build a list of two elements overriding defaults values with creators using size")
        public void shouldBuildAListOfTwoUsingCreatorAndSize() {
            //Given
            int size = 2;

            //When
            List<Pojo> testSimple = Builder.build()
                    .list(creator)
                    .size(size)
                    .override(name, valueCreator)
                    .override(value, valueCreator)
                    .get();

            //Then
            assertThat(testSimple).isNotEmpty();
            assertThat(testSimple).hasSize(size);

            assertThat(testSimple.get(0).getName()).isEqualTo(testSimple.get(1).getName());
            assertThat(testSimple.get(0).getValue()).isEqualTo(testSimple.get(1).getValue());
            assertThat(testSimple.get(0).getName()).isEqualTo("test1");
            assertThat(testSimple.get(0).getValue()).isEqualTo("test1");
        }

        @Test
        @DisplayName("should build a list of one element overriding defaults values with creators")
        public void shouldBuildAListOfTwoUsingCreators() {
            //When
            List<Pojo> testSimple = Builder.build()
                    .list(creator)
                    .elements()
                        .element()
                            .override(name, valueCreator).end()
                        .element()
                            .override(value, valueCreator).end()
                    .get();

            //Then
            assertThat(testSimple).isNotEmpty();
            assertThat(testSimple).hasSize(2);

            assertThat(testSimple.get(0).getName()).isEqualTo("test1");
            assertThat(testSimple.get(1).getValue()).isEqualTo("test1");
        }

        @Test
        @DisplayName("should build a list of one elements using defaults values when there is no size or elements definitions")
        public void shouldBuildAListSizeOneWithNoSizeAndNoElementsDefinitions() {
            //When
            List<Pojo> testSimple = Builder.build()
                    .list(creator).elements().get();

            //Then
            assertThat(testSimple).isNotEmpty();
            assertThat(testSimple).hasSize(1);

            assertThat(testSimple.get(0).getName()).isEqualTo("defaultName");
            assertThat(testSimple.get(0).getValue()).isEqualTo("defaultValue");
        }
    }
}
