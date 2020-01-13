package com.github.koshamo.puri.ui.controls.role;

import com.github.koshamo.puri.setup.PrColors;
import com.github.koshamo.puri.setup.RoleType;

import javafx.scene.control.SkinBase;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/*private*/ class RoleCardSkin extends SkinBase<RoleCard> {

	private final RoleType type;
	
	protected RoleCardSkin(RoleCard control, RoleType type) {
		super(control);
		this.type = type;
		
		drawComponent(false, 0);
		installTooltip(control);
	}

	public void drawComponent(boolean used, int gulden) {
		this.getChildren().clear();
		Pane pane = new Pane();
		
		drawCard(pane);
		drawGulden(pane, gulden);
		drawCross(pane, used);
		
		this.getChildren().add(pane);
	}

	private void drawCard(Pane pane) {
		Rectangle border = new Rectangle(152, 102, Color.BLACK);
		Rectangle rect = new Rectangle(150, 100, PrColors.ROLE.getColor());
		rect.relocate(1, 1);
		Text title = new Text(type.title());
		title.relocate(3, 5);
		Font font = title.getFont();
		title.setFont(new Font(font.getName(), 1.5*font.getSize()));
		Text shortDescription = new Text(type.shorDescription());
		shortDescription.relocate(3, 20);
		
		pane.getChildren().addAll(border, rect, title, shortDescription);
	}

	private static void drawGulden(Pane pane, int gulden) {
		for (int i = 0; i < gulden; i++) {
			Circle border = new Circle(21, Color.BLACK);
			Circle coin = new Circle(20, PrColors.GULDEN.getColor());
			border.setCenterX(31+(i+1)*45/gulden+i*10);
			border.setCenterY(70);
			coin.setCenterX(31+(i+1)*45/gulden+i*10);
			coin.setCenterY(70);
			pane.getChildren().addAll(border, coin);
		}
	}

	private static void drawCross(Pane pane, boolean used) {
		if (used) {
			Line line1 = new Line(6,96,146,6);
			line1.setStroke(Color.RED);
			line1.setStrokeWidth(3);
			Line line2 = new Line(6,6,146,96);
			line2.setStroke(Color.RED);
			line2.setStrokeWidth(3);
			pane.getChildren().addAll(line1, line2);
		}
	}

	private void installTooltip(RoleCard card) {
		Tooltip tooltip = new Tooltip(type.description());
		Tooltip.install(card, tooltip);
	}

}
