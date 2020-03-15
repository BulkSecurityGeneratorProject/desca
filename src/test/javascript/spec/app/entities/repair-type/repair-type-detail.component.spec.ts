/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { DescaTestModule } from '../../../test.module';
import { RepairTypeDetailComponent } from '../../../../../../main/webapp/app/entities/repair-type/repair-type-detail.component';
import { RepairTypeService } from '../../../../../../main/webapp/app/entities/repair-type/repair-type.service';
import { RepairType } from '../../../../../../main/webapp/app/entities/repair-type/repair-type.model';

describe('Component Tests', () => {

    describe('RepairType Management Detail Component', () => {
        let comp: RepairTypeDetailComponent;
        let fixture: ComponentFixture<RepairTypeDetailComponent>;
        let service: RepairTypeService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DescaTestModule],
                declarations: [RepairTypeDetailComponent],
                providers: [
                    RepairTypeService
                ]
            })
            .overrideTemplate(RepairTypeDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(RepairTypeDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RepairTypeService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new RepairType(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.repairType).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
