import { Component, input, output, signal } from '@angular/core';
import { CdkDrag, CdkDragDrop, CdkDropList, DragDropModule } from '@angular/cdk/drag-drop';
import { Package, PackageStatus } from '../../../../shared/models';
import { PackageCardComponent } from '../package-card.component';

@Component({
  selector: 'app-package-board-columns',
  imports: [DragDropModule, PackageCardComponent],
  template: `
    <div class="board-columns">
      @for (status of statuses(); track status) {
        <div class="column">
          <div class="column-header">
            <span class="column-title">{{ status.replace('_', ' ') }}</span>
            <span class="column-count">{{ packagesByStatus()[status].length }}</span>
          </div>

          <div
            class="column-body"
            cdkDropList
            [id]="status"
            [cdkDropListData]="packagesByStatus()[status]"
            [cdkDropListConnectedTo]="connectedDropLists()"
            [cdkDropListEnterPredicate]="canEnterDropList()"
            (cdkDropListDropped)="dropToStatus.emit({ event: $event, status })"
            [class.cdk-drop-list-dragging]="isDragging()"
          >
            @for (pkg of packagesByStatus()[status]; track pkg.id) {
              <div
                cdkDrag
                [cdkDragData]="pkg"
                [cdkDragDisabled]="!draggable()"
                (cdkDragStarted)="isDragging.set(true)"
                (cdkDragEnded)="isDragging.set(false)"
                class="drag-item"
              >
                <app-package-card [pkg]="pkg" [draggable]="draggable()" />
                <div *cdkDragPlaceholder class="drag-placeholder"></div>
              </div>
            }

            @if (!(packagesByStatus()[status]?.length)) {
              <div class="empty-column">Drop packages here</div>
            }
          </div>
        </div>
      }
    </div>
  `,
  styles: [`
    .board-columns {
      display: grid;
      grid-template-columns: repeat(3, 1fr);
      gap: 1.25rem;
      align-items: start;
    }
    @media (max-width: 768px) {
      .board-columns {
        grid-template-columns: 1fr;
      }
    }
    .column {
      background: #f8fafc;
      border-radius: 12px;
      border: 1px solid #e5e7eb;
      overflow: hidden;
    }
    .column-header {
      padding: 0.875rem 1rem;
      background: white;
      border-bottom: 1px solid #e5e7eb;
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
    .column-title {
      font-weight: 700;
      font-size: 0.85rem;
      text-transform: uppercase;
      letter-spacing: 0.05em;
      color: #374151;
    }
    .column-count {
      background: #e5e7eb;
      color: #6b7280;
      font-size: 0.75rem;
      font-weight: 600;
      padding: 0.15rem 0.5rem;
      border-radius: 999px;
    }
    .column-body {
      padding: 0.875rem;
      min-height: 200px;
      display: flex;
      flex-direction: column;
      gap: 0.75rem;
      transition: background 0.2s;
    }
    .column-body.cdk-drop-list-receiving {
      background: #eff6ff;
      border: 2px dashed #3b82f6;
      border-radius: 8px;
    }
    .empty-column {
      display: flex;
      align-items: center;
      justify-content: center;
      height: 80px;
      color: #d1d5db;
      font-size: 0.85rem;
      border: 2px dashed #e5e7eb;
      border-radius: 8px;
    }
    .drag-placeholder {
      height: 80px;
      background: #eff6ff;
      border: 2px dashed #93c5fd;
      border-radius: 10px;
    }
    .drag-item.cdk-drag-disabled {
      opacity: 0.9;
    }
    .cdk-drag-animating {
      transition: transform 250ms cubic-bezier(0, 0, 0.2, 1);
    }
    .cdk-drag-preview {
      box-shadow: 0 8px 32px rgba(0,0,0,0.18);
      border-radius: 10px;
      opacity: 0.95;
    }
  `],
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
