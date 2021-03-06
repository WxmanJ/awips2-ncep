package gov.noaa.nws.ncep.viz.rsc.ncgrid.rsc;

import gov.noaa.nws.ncep.viz.resources.INatlCntrsResourceData;
import gov.noaa.nws.ncep.viz.resources.attributes.AbstractEditResourceAttrsDialog;
import gov.noaa.nws.ncep.viz.resources.attributes.ResourceAttrSet.RscAttrValue;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import com.raytheon.uf.viz.core.exception.VizException;
import com.raytheon.uf.viz.core.rsc.capabilities.Capabilities;

/**
 * The grid contour attribute editing dialog.
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Ticket#     Engineer    Description
 * ------------ ----------  ----------- --------------------------
 * Oct  2010    277         M. Li       Copied form EditGridAttributeDialog
 * Nov,22 2010  352         X. Guo      Add HILO, HLSYM and move all help functions
 *                                      into NcgridAttributesHelp.java
 * Dec 13 2011  578         G. Hull     added ensembleComponentWeights, and folded the 
 *                                      EnsembleSelectDialog into this one.
 * Feb 15 2013              X. Guo      Added skip and filter
 * 03/22/2016   R10366      bkowal      Cleanup. Check for Ensemble GD File updates on close.
 * 04/05/2016   R15715      dgilling    Refactored for new AbstractEditResourceAttrsDialog constructor.                                                                       
 * 
 * @author mli
 * @version 1
 */

public class EditEnsembleAttributesDialog extends
        AbstractEditResourceAttrsDialog {

    private RscAttrValue cintString = null;

    private RscAttrValue glevel = null;

    private RscAttrValue gvcord = null;

    private RscAttrValue skip = null;

    private RscAttrValue filter = null;

    private RscAttrValue scale = null;

    private RscAttrValue gdpfun = null;

    private RscAttrValue type = null;

    private RscAttrValue lineAttr = null;

    private RscAttrValue fint = null;

    private RscAttrValue fline = null;

    private RscAttrValue hilo = null;

    private RscAttrValue hlsym = null;

    private RscAttrValue wind = null;

    private RscAttrValue title = null;

    private RscAttrValue colors = null;

    private RscAttrValue marker = null;

    private RscAttrValue grdlbl = null;

    private Text glevelText;

    private Text gvcordText;

    private Text skipText;

    private Text filterText;

    private Text scaleText;

    private Text gdpfunText;

    private Text typeText;

    private Text cintText;

    private Text lineAttrText;

    private Text fintAttrText;

    private Text flineAttrText;

    private Text hiloAttrText;

    private Text hlsymAttrText;

    private Text windAttrText;

    private Text titleAttrText;

    private Text colorsText;

    private Text markerText;

    private Text grdlblText;

    private EnsembleSelectComposite ensSelComp;

    public EditEnsembleAttributesDialog(Shell parentShell,
            INatlCntrsResourceData rd, Capabilities capabilities, Boolean apply) {
        super(parentShell, rd, capabilities, apply);
    }

    @Override
    public Composite createDialog(Composite composite) {
        lineAttr = editedRscAttrSet.getRscAttr("lineAttributes");
        cintString = editedRscAttrSet.getRscAttr("cint");
        glevel = editedRscAttrSet.getRscAttr("glevel");
        gvcord = editedRscAttrSet.getRscAttr("gvcord");
        skip = editedRscAttrSet.getRscAttr("skip");
        filter = editedRscAttrSet.getRscAttr("filter");
        scale = editedRscAttrSet.getRscAttr("scale");
        gdpfun = editedRscAttrSet.getRscAttr("gdpfun");
        type = editedRscAttrSet.getRscAttr("type");
        fint = editedRscAttrSet.getRscAttr("fint");
        fline = editedRscAttrSet.getRscAttr("fline");
        hilo = editedRscAttrSet.getRscAttr("hilo");
        hlsym = editedRscAttrSet.getRscAttr("hlsym");
        title = editedRscAttrSet.getRscAttr("title");
        colors = editedRscAttrSet.getRscAttr("colors");
        marker = editedRscAttrSet.getRscAttr("marker");
        grdlbl = editedRscAttrSet.getRscAttr("grdlbl");
        wind = editedRscAttrSet.getRscAttr("wind");

        // confirm the classes of the attributes..
        if (lineAttr.getAttrClass() != String.class) {
            statusHandler.warn("line is not of expected class? "
                    + lineAttr.getAttrClass().toString());
        }
        if (cintString.getAttrClass() != String.class) {
            statusHandler.warn("cint is not of expected class? "
                    + cintString.getAttrClass().toString());
        }

        if (cintString == null
                || ((String) cintString.getAttrValue()).trim().length() <= 0) {
            cintString.setAttrValue("");
        }

        if (hilo != null && ((String) hilo.getAttrValue()).trim().length() <= 0) {
            hilo.setAttrValue("");
        }

        if (hlsym != null
                && ((String) hlsym.getAttrValue()).trim().length() <= 0) {
            hlsym.setAttrValue("");
        }

        // Resources Tab
        TabFolder tabFolder = new TabFolder(composite, SWT.TOP);
        tabFolder.setLayoutData(new GridData());
        TabItem resourcesTab = new TabItem(tabFolder, SWT.TOP);
        resourcesTab.setText("Edit Attributes");

        Group topAttrsGrp = new Group(tabFolder, SWT.SHADOW_NONE);
        GridLayout contourAttrGridLayout = new GridLayout();
        contourAttrGridLayout.numColumns = 2;
        contourAttrGridLayout.marginHeight = 8;
        contourAttrGridLayout.marginWidth = 2;
        contourAttrGridLayout.horizontalSpacing = 20;
        contourAttrGridLayout.verticalSpacing = 8;
        topAttrsGrp.setLayout(contourAttrGridLayout);

        resourcesTab.setControl(topAttrsGrp);

        Composite gridAttrsComp = new Composite(topAttrsGrp, SWT.SHADOW_NONE);
        GridLayout contourIntervalsGridLayout = new GridLayout();
        contourIntervalsGridLayout.numColumns = 2;
        gridAttrsComp.setLayout(contourIntervalsGridLayout);

        // GLEVEL
        Label glevelLabel = new Label(gridAttrsComp, SWT.NONE);
        glevelLabel.setText("GLEVEL:");
        glevelText = new Text(gridAttrsComp, SWT.SINGLE | SWT.BORDER);
        glevelText.setLayoutData(new GridData(600, SWT.DEFAULT));
        glevelText.setText((String) glevel.getAttrValue());
        glevelText.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                glevel.setAttrValue(glevelText.getText().trim());
            }

        });

        // GVCORD
        Label gvcordLabel = new Label(gridAttrsComp, SWT.NONE);
        gvcordLabel.setText("GVCORD:");
        gvcordText = new Text(gridAttrsComp, SWT.SINGLE | SWT.BORDER);
        gvcordText.setLayoutData(new GridData(600, SWT.DEFAULT));
        gvcordText.setText((String) gvcord.getAttrValue());
        gvcordText.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                gvcord.setAttrValue(gvcordText.getText().trim());
            }
        });

        // SKIP
        Label skipLabel = new Label(gridAttrsComp, SWT.NONE);
        skipLabel.setText("SKIP:");
        skipText = new Text(gridAttrsComp, SWT.SINGLE | SWT.BORDER);
        skipText.setLayoutData(new GridData(600, SWT.DEFAULT));
        skipText.setText((String) skip.getAttrValue());
        skipText.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                skip.setAttrValue(skipText.getText().trim());
            }
        });

        // filter
        Label filterLabel = new Label(gridAttrsComp, SWT.NONE);
        filterLabel.setText("FILTER:");
        filterText = new Text(gridAttrsComp, SWT.SINGLE | SWT.BORDER);
        filterText.setLayoutData(new GridData(600, SWT.DEFAULT));
        filterText.setText((String) filter.getAttrValue());
        filterText.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                filter.setAttrValue(filterText.getText().trim());
            }
        });

        // SCALE
        Label scaleLabel = new Label(gridAttrsComp, SWT.NONE);
        scaleLabel.setText("SCALE:");
        scaleText = new Text(gridAttrsComp, SWT.SINGLE | SWT.BORDER);
        scaleText.setLayoutData(new GridData(600, SWT.DEFAULT));
        scaleText.setText((String) scale.getAttrValue());
        scaleText.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                scale.setAttrValue(scaleText.getText().trim());
            }
        });

        // GDPFUN
        Label gdpfunLabel = new Label(gridAttrsComp, SWT.NONE);
        gdpfunLabel.setText("GDPFUN:");
        gdpfunText = new Text(gridAttrsComp, SWT.SINGLE | SWT.BORDER);
        gdpfunText.setLayoutData(new GridData(600, SWT.DEFAULT));
        gdpfunText.setText((String) gdpfun.getAttrValue());
        gdpfunText.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                gdpfun.setAttrValue(gdpfunText.getText().trim());
            }
        });

        // Type
        Label typeLabel = new Label(gridAttrsComp, SWT.NONE);
        typeLabel.setText("TYPE:");
        typeText = new Text(gridAttrsComp, SWT.SINGLE | SWT.BORDER);
        typeText.setLayoutData(new GridData(600, SWT.DEFAULT));
        typeText.setText((String) type.getAttrValue());
        typeText.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                type.setAttrValue(typeText.getText().trim());
            }
        });

        // Contour Intervals -- CINT
        Label contourIntervalsLabel = new Label(gridAttrsComp, SWT.NONE);
        contourIntervalsLabel.setText("CINT:");
        cintText = new Text(gridAttrsComp, SWT.SINGLE | SWT.BORDER);
        cintText.setLayoutData(new GridData(600, SWT.DEFAULT));
        cintText.setText((String) cintString.getAttrValue());
        cintText.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                cintString.setAttrValue(cintText.getText().trim());
            }

        });

        // LINE
        Label lineAttrLabel = new Label(gridAttrsComp, SWT.NONE);
        lineAttrLabel.setText("LINE:");
        lineAttrText = new Text(gridAttrsComp, SWT.SINGLE | SWT.BORDER);
        lineAttrText.setLayoutData(new GridData(600, SWT.DEFAULT));
        lineAttrText.setText((String) lineAttr.getAttrValue());
        lineAttrText.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                lineAttr.setAttrValue(lineAttrText.getText().trim());
            }
        });

        // FINT
        Label fintAttrLabel = new Label(gridAttrsComp, SWT.NONE);
        fintAttrLabel.setText("FINT:");
        fintAttrText = new Text(gridAttrsComp, SWT.SINGLE | SWT.BORDER);
        fintAttrText.setLayoutData(new GridData(600, SWT.DEFAULT));
        fintAttrText.setText((String) fint.getAttrValue());
        fintAttrText.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                fint.setAttrValue(fintAttrText.getText().trim());
            }
        });

        // FLINE
        Label flineAttrLabel = new Label(gridAttrsComp, SWT.NONE);
        flineAttrLabel.setText("FLINE:");
        flineAttrText = new Text(gridAttrsComp, SWT.SINGLE | SWT.BORDER);
        flineAttrText.setLayoutData(new GridData(600, SWT.DEFAULT));
        flineAttrText.setText((String) fline.getAttrValue());
        flineAttrText.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                fline.setAttrValue(flineAttrText.getText().trim());
            }
        });

        // HILO
        if (hilo != null) {
            Label hiloAttrLabel = new Label(gridAttrsComp, SWT.NONE);
            hiloAttrLabel.setText("HILO:");
            hiloAttrText = new Text(gridAttrsComp, SWT.SINGLE | SWT.BORDER);
            hiloAttrText.setLayoutData(new GridData(600, SWT.DEFAULT));
            hiloAttrText.setText((String) hilo.getAttrValue());
            hiloAttrText.addModifyListener(new ModifyListener() {
                @Override
                public void modifyText(ModifyEvent e) {
                    hilo.setAttrValue(hiloAttrText.getText().trim());
                }
            });
        }

        // HLSYM
        if (hlsym != null) {
            Label hlsymAttrLabel = new Label(gridAttrsComp, SWT.NONE);
            hlsymAttrLabel.setText("HLSYM:");
            hlsymAttrText = new Text(gridAttrsComp, SWT.SINGLE | SWT.BORDER);
            hlsymAttrText.setLayoutData(new GridData(600, SWT.DEFAULT));
            hlsymAttrText.setText((String) hlsym.getAttrValue());
            hlsymAttrText.addModifyListener(new ModifyListener() {
                @Override
                public void modifyText(ModifyEvent e) {
                    hlsym.setAttrValue(hlsymAttrText.getText().trim());
                }
            });
        }

        // WIND
        Label windAttrLabel = new Label(gridAttrsComp, SWT.NONE);
        windAttrLabel.setText("WIND:");
        windAttrText = new Text(gridAttrsComp, SWT.SINGLE | SWT.BORDER);
        windAttrText.setLayoutData(new GridData(600, SWT.DEFAULT));
        windAttrText.setText((String) wind.getAttrValue());
        windAttrText.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                wind.setAttrValue(windAttrText.getText().trim());
            }
        });

        // TITLE
        Label titleAttrLabel = new Label(gridAttrsComp, SWT.NONE);
        titleAttrLabel.setText("TITLE:");
        titleAttrText = new Text(gridAttrsComp, SWT.SINGLE | SWT.BORDER);
        titleAttrText.setLayoutData(new GridData(600, SWT.DEFAULT));
        titleAttrText.setText((String) title.getAttrValue());
        titleAttrText.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                title.setAttrValue(titleAttrText.getText().trim());
            }
        });

        // COLORS
        Label colorsLabel = new Label(gridAttrsComp, SWT.NONE);
        colorsLabel.setText("COLORS:");
        colorsText = new Text(gridAttrsComp, SWT.SINGLE | SWT.BORDER);
        colorsText.setLayoutData(new GridData(600, SWT.DEFAULT));
        colorsText.setText((String) colors.getAttrValue());
        colorsText.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                colors.setAttrValue(colorsText.getText().trim());
            }
        });

        // MARKER
        Label markerLabel = new Label(gridAttrsComp, SWT.NONE);
        markerLabel.setText("MARKER:");
        markerText = new Text(gridAttrsComp, SWT.SINGLE | SWT.BORDER);
        markerText.setLayoutData(new GridData(600, SWT.DEFAULT));
        markerText.setText((String) marker.getAttrValue());
        markerText.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                marker.setAttrValue(markerText.getText().trim());
            }
        });

        // GRDLBL
        Label grdlblLabel = new Label(gridAttrsComp, SWT.NONE);
        grdlblLabel.setText("GRDLBL:");
        grdlblText = new Text(gridAttrsComp, SWT.SINGLE | SWT.BORDER);
        grdlblText.setLayoutData(new GridData(600, SWT.DEFAULT));
        grdlblText.setText(String.valueOf(grdlbl.getAttrValue()));
        grdlblText.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                grdlbl.setAttrValue(Integer
                        .valueOf(grdlblText.getText().trim()));
            }
        });

        final Button toolTipDisplay = new Button(gridAttrsComp, SWT.CHECK);
        toolTipDisplay.setLayoutData(new GridData(SWT.DEFAULT, SWT.DEFAULT));
        toolTipDisplay.setText("ToolTips OFF");
        toolTipDisplay.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                if (toolTipDisplay.getSelection()) {
                    toolTipDisplay.setText("ToolTips ON");
                    enableToolTip(true);
                } else {
                    toolTipDisplay.setText("ToolTips OFF");
                    enableToolTip(false);
                }
            }
        });

        ensSelComp = new EnsembleSelectComposite(tabFolder);
        ensSelComp.setLayout(new GridLayout());

        // Tab 2
        TabItem weightsTab = new TabItem(tabFolder, SWT.TOP);
        weightsTab.setText("Ensemble Weighting");
        weightsTab.setControl(ensSelComp);

        try {
            ensSelComp.init((NcEnsembleResourceData) rscData, editedRscAttrSet);
        } catch (VizException e1) {
            statusHandler.error(
                    "Failed to initialize the Ensemble Select Composite.", e1);
        }

        return composite;
    }

    @Override
    protected void handleOK() {
        this.ensSelComp.updateGDFile();
        super.handleOK();
    }

    private void enableToolTip(boolean on) {
        if (on) {
            glevelText.setToolTipText(NcgridAttributesHelp.GlevelToolTipText());
            gvcordText.setToolTipText(NcgridAttributesHelp.GvcordToolTipText());
            skipText.setToolTipText(NcgridAttributesHelp.SkipToolTipText());
            filterText.setToolTipText(NcgridAttributesHelp.FilterToolTipText());
            scaleText.setToolTipText(NcgridAttributesHelp.ScaleToolTipText());
            gdpfunText.setToolTipText(NcgridAttributesHelp.GdpfunToolTipText());
            typeText.setToolTipText(NcgridAttributesHelp.TypeToolTipText());
            cintText.setToolTipText(NcgridAttributesHelp.CintToolTipText());
            lineAttrText.setToolTipText(NcgridAttributesHelp.LineToolTipText());
            fintAttrText.setToolTipText(NcgridAttributesHelp.FintToolTipText());
            flineAttrText.setToolTipText(NcgridAttributesHelp
                    .FlineToolTipText());
            if (hilo != null) {
                hiloAttrText.setToolTipText(NcgridAttributesHelp
                        .HiloToolTipText());
            }
            if (hlsym != null) {
                hlsymAttrText.setToolTipText(NcgridAttributesHelp
                        .HlsymToolTipText());
            }
            titleAttrText.setToolTipText(NcgridAttributesHelp
                    .TitleToolTipText());
            colorsText.setToolTipText(NcgridAttributesHelp.ColorsToolTipText());
            markerText.setToolTipText(NcgridAttributesHelp.MarkerToolTipText());
            grdlblText.setToolTipText(NcgridAttributesHelp.GrdlblToolTipText());
            windAttrText.setToolTipText(NcgridAttributesHelp.WindToolTipText());
        } else {
            glevelText.setToolTipText(null);
            gvcordText.setToolTipText(null);
            skipText.setToolTipText(null);
            filterText.setToolTipText(null);
            scaleText.setToolTipText(null);
            gdpfunText.setToolTipText(null);
            typeText.setToolTipText(null);
            cintText.setToolTipText(null);
            lineAttrText.setToolTipText(null);
            fintAttrText.setToolTipText(null);
            flineAttrText.setToolTipText(null);
            if (hilo != null) {
                hiloAttrText.setToolTipText(null);
            }
            if (hlsym != null) {
                hlsymAttrText.setToolTipText(null);
            }
            titleAttrText.setToolTipText(null);
            colorsText.setToolTipText(null);
            markerText.setToolTipText(null);
            grdlblText.setToolTipText(null);
            windAttrText.setToolTipText(null);
        }
    }

    @Override
    public void initWidgets() {
    }
}
