import { Component, input, output, signal } from '@angular/core';
import { CdkDrag, CdkDragDrop, CdkDropList, DragDropModule } from '@angular/cdk/drag-drop';
import { Package, PackageStatus } from '../../../../shared/models';
import { PackageCardComponent } from '../package-card.component';

@Component({
  selector: 'app-package-board-columns',
  imports: [DragDropModule, PackageCardComponent],
  templateUrl: './package-board-columns.component.html',
  styleUrl: './package-board-columns.component.css',
})
export class PackageBoardColumnsComponent {
  readonly statuses = input.required<PackageStatus[]>();
  readonly packagesByStatus = input.required<Record<PackageStatus, Package[]>>();
  readonly connectedDropLists = input.required<PackageStatus[]>();
  readonly draggable = input<boolean>(false);
  readonly canEnterDropList = input.required<(drag: CdkDrag<Package>, drop: CdkDropList<Package[]>) => boolean>();

  readonly dropToStatus = output<{ event: CdkDragDrop<Package[]>; status: PackageStatus }>();
  protected readonly isDragging = signal(false);
}
