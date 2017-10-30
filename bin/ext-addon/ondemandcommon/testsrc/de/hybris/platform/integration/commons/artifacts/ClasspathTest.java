package de.hybris.platform.integration.commons.artifacts;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.integration.commons.artifacts.jaxb.ClasspathType;
import de.hybris.platform.integration.commons.artifacts.jaxb.ClasspathentryType;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.Collection;

import static com.google.common.collect.Lists.newArrayList;
import static java.lang.Boolean.FALSE;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang.StringUtils.equalsIgnoreCase;
import static org.apache.commons.lang.StringUtils.isBlank;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

@UnitTest
public class ClasspathTest {
    private static final String ECLIPSE_CLASSPATH_FILE = "classpath:ondemandcommon/test/replicated.classpath.xml";
    private static final String LIBRARY = "lib";
    private static final String BLANK = "";
    private static final String SOURCE = "src";
    private static final String CONTAINER = "con";
    private static final String OUTPUT = "output";

    private final PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();

    private File classpathFile;
    private ClasspathType eclipseClasspath;

    private static final ClasspathentryType[] ALL_REFERENCES = newArrayList(
            exportedAndNotCombinedSource("/acceleratorservices"),
            exportedAndNotCombinedSource("/basecommerce"),
            exportedAndNotCombinedSource("/hmc"),
            exportedAndNotCombinedSource("/corepluscommons"),
            exportedAndNotCombinedSource("/commerceservices"),
            exportedLibrary("resources"),
            exportedLibrary("lib/archaius-core-0.5.6.jar"),
            exportedLibrary("lib/mimepull-1.6.jar"),
            exportedLibrary("lib/httpclient-4.2.6.jar"),
            exportedLibrary("lib/hystrix-core-1.2.8.jar"),
            exportedLibrary("bin/ondemandcommonserver.jar"),
            unexportedAndNotCombinedSource("/platform"),
            sourceOnly("testsrc"),
            container("org.eclipse.jdt.launching.JRE_CONTAINER"),
            output("eclipsebin/notused")
    ).toArray(new ClasspathentryType[0]);

    private static final ClasspathentryType[] EXPECTED_LIBRARY_REFERENCES = classpathLibraries(ALL_REFERENCES).toArray(new ClasspathentryType[0]);

    private static final ClasspathentryType[] EXPECTED_EXPORTED_AND_UNCOMBINED_SOURCE_REFERENCES = classpathExportedAndNotCombinedSource(ALL_REFERENCES).toArray(new ClasspathentryType[0]);

    private static final ClasspathentryType[] EXPECTED_UNEXPORTED_AND_UNCOMBINED_SOURCE_REFERENCES = classpathUnexportedAndNotCombinedSource(ALL_REFERENCES).toArray(new ClasspathentryType[0]);

    private static final ClasspathentryType[] EXPECTED_SOURCE_ONLY_REFERENCES = classpathSourceOnly(ALL_REFERENCES).toArray(new ClasspathentryType[0]);

    private static final ClasspathentryType[] EXPECTED_CONTAINER_REFERENCES = containers(ALL_REFERENCES).toArray(new ClasspathentryType[0]);

    private static final ClasspathentryType[] EXPECTED_OUTPUT_REFERENCES = output(ALL_REFERENCES).toArray(new ClasspathentryType[0]);



    @Before
    public void setup() throws Exception {
        classpathFile = getClasspathFileHandle();
        eclipseClasspath = parseClasspathFile();
    }

    private File getClasspathFileHandle() throws Exception {
        final Resource resource = pathMatchingResourcePatternResolver.getResourceLoader().getResource(ECLIPSE_CLASSPATH_FILE);
        return resource.getFile();
    }

    private ClasspathType parseClasspathFile() throws Exception {
        final JAXBContext jc = JAXBContext.newInstance("de.hybris.platform.integration.commons.artifacts.jaxb");
        Unmarshaller u = jc.createUnmarshaller();
        final JAXBElement element = (JAXBElement)u.unmarshal(classpathFile);

        return (ClasspathType)element.getValue();
    }

    @Test
    public void classpath_file_eixsts(){
        assertThat(classpathFile.exists(), is(true));
    }

    @Test
    public void can_parse_eclipse_file() {
        assertThat(eclipseClasspath, is(notNullValue()));
    }

    @Test
    public void classpathFile_contains_correct_number_of_references() {
        assertThat(eclipseClasspath.getClasspathentry(), Matchers.hasSize(15));
    }

    @Test
    public void classpathFile_contains_correct_library_references() {
        assertThat(classpathLibraries(), containsInAnyOrder(EXPECTED_LIBRARY_REFERENCES));
    }

    @Test
    public void classpathFile_contains_correct_uncombined_and_exported_source_references() {
        assertThat(classpathExportedAndNotCombinedSource(), containsInAnyOrder(EXPECTED_EXPORTED_AND_UNCOMBINED_SOURCE_REFERENCES));
    }

    @Test
    public void classpathFile_contains_correct_uncombined_and_unexported_source_references() {
        assertThat(classpathUnexportedAndNotCombinedSource(), containsInAnyOrder(EXPECTED_UNEXPORTED_AND_UNCOMBINED_SOURCE_REFERENCES));
    }

    @Test
    public void classpathFile_contains_correct_source_only_references() {
        assertThat(classpathSourceOnly(), containsInAnyOrder(EXPECTED_SOURCE_ONLY_REFERENCES));
    }

    @Test
    public void classpathFile_contains_correct_container_references() {
        assertThat(containers(), containsInAnyOrder(EXPECTED_CONTAINER_REFERENCES));
    }

    @Test
    public void classpathFile_contains_correct_output_references() {
        assertThat(output(), containsInAnyOrder(EXPECTED_OUTPUT_REFERENCES));
    }

    @Test
    public void classpathFile_contains_all_expected_references() {
        assertThat(eclipseClasspath.getClasspathentry(), containsInAnyOrder(ALL_REFERENCES));
    }


    public static ClasspathentryType exportedLibrary(String libraryPath) {
        ClasspathentryType classpathentryType = new ClasspathentryType();
        classpathentryType.setExported(Boolean.TRUE.toString());
        classpathentryType.setKind(LIBRARY);
        classpathentryType.setPath(libraryPath);
        classpathentryType.setValue(BLANK);
        return classpathentryType;
    }

    public static ClasspathentryType exportedAndNotCombinedSource(String sourcePath) {
        ClasspathentryType classpathentryType = new ClasspathentryType();
        classpathentryType.setExported(Boolean.TRUE.toString());
        classpathentryType.setKind(SOURCE);
        classpathentryType.setCombineaccessrules(FALSE.toString());
        classpathentryType.setPath(sourcePath);
        classpathentryType.setValue(BLANK);
        return classpathentryType;
    }

    public static ClasspathentryType unexportedAndNotCombinedSource(String sourcePath) {
        ClasspathentryType classpathentryType = new ClasspathentryType();
        classpathentryType.setKind(SOURCE);
        classpathentryType.setPath(sourcePath);
        classpathentryType.setValue(BLANK);
        classpathentryType.setCombineaccessrules(FALSE.toString());
        return classpathentryType;
    }

    public static ClasspathentryType sourceOnly(String sourcePath) {
        ClasspathentryType classpathentryType = new ClasspathentryType();
        classpathentryType.setKind(SOURCE);
        classpathentryType.setPath(sourcePath);
        classpathentryType.setValue(BLANK);
        return classpathentryType;
    }

    public static ClasspathentryType container(String path) {
        ClasspathentryType classpathentryType = new ClasspathentryType();
        classpathentryType.setExported(Boolean.TRUE.toString());
        classpathentryType.setKind(CONTAINER);
        classpathentryType.setPath(path);
        classpathentryType.setValue(BLANK);
        return classpathentryType;
    }

    public static ClasspathentryType output(String path) {
        ClasspathentryType classpathentryType = new ClasspathentryType();
        classpathentryType.setKind(OUTPUT);
        classpathentryType.setPath(path);
        classpathentryType.setValue(BLANK);
        return classpathentryType;
    }

    private Collection<ClasspathentryType> classpathLibraries() {
        return classpathLibraries(eclipseClasspath.getClasspathentry());
    }

    private Collection<ClasspathentryType> classpathExportedAndNotCombinedSource() {
        return classpathExportedAndNotCombinedSource(eclipseClasspath.getClasspathentry());
    }

    private Collection<ClasspathentryType> classpathUnexportedAndNotCombinedSource() {
        return classpathUnexportedAndNotCombinedSource(eclipseClasspath.getClasspathentry());
    }

    private Collection<ClasspathentryType> classpathSourceOnly() {
        return classpathSourceOnly(eclipseClasspath.getClasspathentry());
    }

    private Collection<ClasspathentryType> containers() {
        return containers(eclipseClasspath.getClasspathentry());
    }

    private Collection<ClasspathentryType> output() {
        return output(eclipseClasspath.getClasspathentry());
    }

    private static Collection<ClasspathentryType> classpathLibraries(final ClasspathentryType[] references) {
        return classpathLibraries(newArrayList(references));
    }

    private static Collection<ClasspathentryType> classpathLibraries(final Collection<ClasspathentryType> references) {
        return references
                .stream()
                .filter(entry -> equalsIgnoreCase(LIBRARY, entry.getKind()))
                .collect(toList());
    }

    private static Collection<ClasspathentryType> classpathExportedAndNotCombinedSource(final ClasspathentryType[] references) {
        return classpathExportedAndNotCombinedSource(newArrayList(references));
    }

    private static Collection<ClasspathentryType> classpathExportedAndNotCombinedSource(final Collection<ClasspathentryType> references) {
        return references
                .stream()
                .filter(entry -> equalsIgnoreCase(SOURCE, entry.getKind())
                        && equalsIgnoreCase(FALSE.toString(), entry.getCombineaccessrules())
                        && equalsIgnoreCase(Boolean.TRUE.toString(), entry.getExported()))
                .collect(toList());
    }

    private static Collection<ClasspathentryType> classpathUnexportedAndNotCombinedSource(final ClasspathentryType[] references) {
        return classpathUnexportedAndNotCombinedSource(newArrayList(references));
    }

    private static Collection<ClasspathentryType> classpathUnexportedAndNotCombinedSource(final Collection<ClasspathentryType> references) {
        return references
                .stream()
                .filter(entry -> equalsIgnoreCase(SOURCE, entry.getKind())
                        && equalsIgnoreCase(FALSE.toString(), entry.getCombineaccessrules())
                        && isBlank(entry.getExported()))
                .collect(toList());
    }

    private static Collection<ClasspathentryType> classpathSourceOnly(final ClasspathentryType[] references) {
        return classpathSourceOnly(newArrayList(references));
    }

    private static Collection<ClasspathentryType> classpathSourceOnly(final Collection<ClasspathentryType> references) {
        return references
                .stream()
                .filter(entry -> equalsIgnoreCase(SOURCE, entry.getKind())
                        && isBlank(entry.getCombineaccessrules())
                        && isBlank(entry.getExported()))
                .collect(toList());
    }

    private static Collection<ClasspathentryType> containers(final ClasspathentryType[] references) {
        return containers(newArrayList(references));
    }

    private static Collection<ClasspathentryType> containers(final Collection<ClasspathentryType> references) {
        return references
                .stream()
                .filter(entry -> equalsIgnoreCase(CONTAINER, entry.getKind()))
                .collect(toList());
    }

    private static Collection<ClasspathentryType> output(final ClasspathentryType[] references) {
        return output(newArrayList(references));
    }

    private static Collection<ClasspathentryType> output(final Collection<ClasspathentryType> references) {
        return references
                .stream()
                .filter(entry -> equalsIgnoreCase(OUTPUT, entry.getKind()))
                .collect(toList());
    }


}
