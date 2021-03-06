/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2017 iText Group NV
    Authors: iText Software.

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License version 3
    as published by the Free Software Foundation with the addition of the
    following permission added to Section 15 as permitted in Section 7(a):
    FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
    ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
    OF THIRD PARTY RIGHTS

    This program is distributed in the hope that it will be useful, but
    WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
    or FITNESS FOR A PARTICULAR PURPOSE.
    See the GNU Affero General Public License for more details.
    You should have received a copy of the GNU Affero General Public License
    along with this program; if not, see http://www.gnu.org/licenses or write to
    the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
    Boston, MA, 02110-1301 USA, or download the license from the following URL:
    http://itextpdf.com/terms-of-use/

    The interactive user interfaces in modified source and object code versions
    of this program must display Appropriate Legal Notices, as required under
    Section 5 of the GNU Affero General Public License.

    In accordance with Section 7(b) of the GNU Affero General Public License,
    a covered work must retain the producer line in every PDF that is created
    or manipulated using iText.

    You can be released from the requirements of the license by purchasing
    a commercial license. Buying such a license is mandatory as soon as you
    develop commercial activities involving the iText software without
    disclosing the source code of your own applications.
    These activities include: offering paid services to customers as an ASP,
    serving PDFs on the fly in a web application, shipping iText with a closed
    source product.

    For more information, please contact iText Software Corp. at this
    address: sales@itextpdf.com
 */
package com.itextpdf.forms;

import com.itextpdf.forms.fields.PdfButtonFormField;
import com.itextpdf.forms.fields.PdfChoiceFormField;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.forms.fields.PdfTextFormField;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.utils.CompareTool;
import com.itextpdf.test.ExtendedITextTest;
import com.itextpdf.test.annotations.type.IntegrationTest;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(IntegrationTest.class)
public class PdfFormFieldTest extends ExtendedITextTest {

    public static final String sourceFolder = "./src/test/resources/com/itextpdf/forms/PdfFormFieldTest/";
    public static final String destinationFolder = "./target/test/com/itextpdf/forms/PdfFormFieldTest/";

    @BeforeClass
    public static void beforeClass() {
        createDestinationFolder(destinationFolder);
    }

    @Test
    public void formFieldTest01() throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(sourceFolder + "formFieldFile.pdf"));

        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, false);

        Map<String, PdfFormField> fields = form.getFormFields();
        PdfFormField field = fields.get("Text1");

        Assert.assertTrue(fields.size() == 6);
        Assert.assertTrue(field.getFieldName().toUnicodeString().equals("Text1"));
        Assert.assertTrue(field.getValue().toString().equals("TestField"));
    }

    @Test
    public void formFieldTest02() throws IOException, InterruptedException {
        String filename = destinationFolder + "formFieldTest02.pdf";
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(filename));

        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);
        Rectangle rect = new Rectangle(210, 490, 150, 22);
        PdfTextFormField field = PdfFormField.createText(pdfDoc, rect, "fieldName", "some value");
        form.addField(field);

        pdfDoc.close();

        CompareTool compareTool = new CompareTool();
        String errorMessage = compareTool.compareByContent(filename, sourceFolder + "cmp_formFieldTest02.pdf", destinationFolder, "diff_");
        if (errorMessage != null) {
            Assert.fail(errorMessage);
        }
    }

    @Test
    public void formFieldTest03() throws IOException, InterruptedException {
        String filename = destinationFolder + "formFieldTest03.pdf";
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(sourceFolder + "formFieldFile.pdf"), new PdfWriter(filename));

        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);

        PdfPage page = pdfDoc.getFirstPage();
        Rectangle rect = new Rectangle(210, 490, 150, 22);

        PdfTextFormField field = PdfFormField.createText(pdfDoc, rect, "TestField", "some value");

        form.addField(field, page);

        pdfDoc.close();

        CompareTool compareTool = new CompareTool();
        String errorMessage = compareTool.compareByContent(filename, sourceFolder + "cmp_formFieldTest03.pdf", destinationFolder, "diff_");
        if (errorMessage != null) {
            Assert.fail(errorMessage);
        }
    }

    @Test
    public void choiceFieldTest01() throws IOException, InterruptedException {
        String filename = destinationFolder + "choiceFieldTest01.pdf";
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(filename));

        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);

        Rectangle rect = new Rectangle(210, 490, 150, 20);

        String[] options = new String[]{"First Item", "Second Item", "Third Item", "Fourth Item"};
        PdfChoiceFormField choice = PdfFormField.createComboBox(pdfDoc, rect, "TestField", "First Item", options);

        form.addField(choice);

        Rectangle rect1 = new Rectangle(210, 250, 150, 90);

        PdfChoiceFormField choice1 = PdfFormField.createList(pdfDoc, rect1, "TestField1", "Second Item", options);
        choice1.setMultiSelect(true);
        form.addField(choice1);

        pdfDoc.close();

        CompareTool compareTool = new CompareTool();
        String errorMessage = compareTool.compareByContent(filename, sourceFolder + "cmp_choiceFieldTest01.pdf", destinationFolder, "diff_");
        if (errorMessage != null) {
            Assert.fail(errorMessage);
        }
    }

    @Test
    public void buttonFieldTest01() throws IOException, InterruptedException {
        String filename = destinationFolder + "buttonFieldTest01.pdf";
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(filename));

        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);

        Rectangle rect = new Rectangle(36, 700, 20, 20);
        Rectangle rect1 = new Rectangle(36, 680, 20, 20);

        PdfButtonFormField group = PdfFormField.createRadioGroup(pdfDoc, "TestGroup", "1");

        PdfFormField.createRadioButton(pdfDoc, rect, group, "1");
        PdfFormField.createRadioButton(pdfDoc, rect1, group, "2");

        form.addField(group);

        PdfButtonFormField pushButton = PdfFormField.createPushButton(pdfDoc, new Rectangle(36, 650, 40, 20), "push", "Capcha");
        PdfButtonFormField checkBox = PdfFormField.createCheckBox(pdfDoc, new Rectangle(36, 560, 20, 20), "TestCheck", "1");

        form.addField(pushButton);
        form.addField(checkBox);

        pdfDoc.close();

        CompareTool compareTool = new CompareTool();
        String errorMessage = compareTool.compareByContent(filename, sourceFolder + "cmp_buttonFieldTest01.pdf", destinationFolder, "diff_");
        if (errorMessage != null) {
            Assert.fail(errorMessage);
        }
    }

    @Test
    public void buttonFieldTest02() throws IOException, InterruptedException {
        String filename = destinationFolder + "buttonFieldTest02.pdf";
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(sourceFolder + "buttonFieldTest02_input.pdf"), new PdfWriter(filename));

        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);

        ((PdfButtonFormField) form.getField("push")).setImage(sourceFolder + "Desert.jpg");

        pdfDoc.close();

        CompareTool compareTool = new CompareTool();
        String errorMessage = compareTool.compareByContent(filename, sourceFolder + "cmp_buttonFieldTest02.pdf", destinationFolder, "diff_");
        if (errorMessage != null) {
            Assert.fail(errorMessage);
        }
    }

    @Test
    public void realFontSizeRegenerateAppearanceTest() throws IOException, InterruptedException {
        String sourceFilename = sourceFolder + "defaultAppearanceRealFontSize.pdf";
        String destFilename = destinationFolder + "realFontSizeRegenerateAppearance.pdf";
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(sourceFilename), new PdfWriter(destFilename));

        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);

        form.getField("fieldName").regenerateField();

        pdfDoc.close();

        CompareTool compareTool = new CompareTool();
        String errorMessage = compareTool.compareByContent(destFilename, sourceFolder + "cmp_realFontSizeRegenerateAppearance.pdf", destinationFolder, "diff_");
        if (errorMessage != null) {
            Assert.fail(errorMessage);
        }
    }

    @Test
    public void addFieldWithKidsTest() {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(new ByteArrayOutputStream()));

        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);

        PdfFormField root = PdfFormField.createEmptyField(pdfDoc);
        root.setFieldName("root");

        PdfFormField child = PdfFormField.createEmptyField(pdfDoc);
        child.setFieldName("child");
        root.addKid(child);

        PdfTextFormField text1 = PdfFormField.createText(pdfDoc, new Rectangle(100, 700, 200, 20), "text1", "test");
        child.addKid(text1);

        form.addField(root);

        Assert.assertEquals(3, form.getFormFields().size());
    }

    @Test
    public void fillFormWithDefaultResources() throws IOException, InterruptedException {
        String outPdf = destinationFolder + "fillFormWithDefaultResources.pdf";
        String cmpPdf = sourceFolder + "cmp_fillFormWithDefaultResources.pdf";

        PdfWriter writer = new PdfWriter(outPdf);
        PdfReader reader = new PdfReader(sourceFolder + "formWithDefaultResources.pdf");
        PdfDocument pdfDoc = new PdfDocument(reader, writer);

        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);


        Map<String, PdfFormField> fields = form.getFormFields();
        PdfFormField field = fields.get("Text1");

        field.setValue("New value size must be 8");
        pdfDoc.close();

        CompareTool compareTool = new CompareTool();
        String errorMessage = compareTool.compareByContent(outPdf, cmpPdf, destinationFolder, "diff_");
        if (errorMessage != null) {
            Assert.fail(errorMessage);
        }
    }

    @Test
    public void fillFormTwiceWithoutResources() throws IOException, InterruptedException {
        String outPdf = destinationFolder + "fillFormWithoutResources.pdf";
        String cmpPdf = sourceFolder + "cmp_fillFormWithoutResources.pdf";

        PdfWriter writer = new PdfWriter(outPdf);
        PdfReader reader = new PdfReader(sourceFolder + "formWithoutResources.pdf");
        PdfDocument pdfDoc = new PdfDocument(reader, writer);

        PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);


        Map<String, PdfFormField> fields = form.getFormFields();
        PdfFormField field = fields.get("Text1");

        field.setValue("New value size must be 8").setFontSize(8);
        pdfDoc.close();

        CompareTool compareTool = new CompareTool();
        String errorMessage = compareTool.compareByContent(outPdf, cmpPdf, destinationFolder, "diff_");
        if (errorMessage != null) {
            Assert.fail(errorMessage);
        }
    }
}
