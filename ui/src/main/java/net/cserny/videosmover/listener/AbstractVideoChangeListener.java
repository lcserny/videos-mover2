package net.cserny.videosmover.listener;

import net.cserny.videosmover.component.CustomTextFieldCell;
import net.cserny.videosmover.model.SimpleVideoOutput;
import net.cserny.videosmover.model.VideoRow;
import net.cserny.videosmover.service.OutputResolver;

abstract class AbstractVideoChangeListener {
    final VideoRow videoRow;
    private final CustomTextFieldCell outputCell;
    private final OutputResolver outputResolver;

    AbstractVideoChangeListener(VideoRow videoRow, CustomTextFieldCell outputCell, OutputResolver outputResolver) {
        this.videoRow = videoRow;
        this.outputCell = outputCell;
        this.outputResolver = outputResolver;
    }

    void processChange(boolean newValue) {
        String outputString = "";
        if (newValue) {
            SimpleVideoOutput videoOutput = outputResolver.resolve(videoRow.getVideo());
            outputString = videoOutput.getOutput();

            // TODO: this doesn't work...
            outputCell.changeColors();
        }
        videoRow.setOutput(outputString);
    }
}
