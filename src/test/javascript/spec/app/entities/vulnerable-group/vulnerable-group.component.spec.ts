/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { DescaTestModule } from '../../../test.module';
import { VulnerableGroupComponent } from '../../../../../../main/webapp/app/entities/vulnerable-group/vulnerable-group.component';
import { VulnerableGroupService } from '../../../../../../main/webapp/app/entities/vulnerable-group/vulnerable-group.service';
import { VulnerableGroup } from '../../../../../../main/webapp/app/entities/vulnerable-group/vulnerable-group.model';

describe('Component Tests', () => {

    describe('VulnerableGroup Management Component', () => {
        let comp: VulnerableGroupComponent;
        let fixture: ComponentFixture<VulnerableGroupComponent>;
        let service: VulnerableGroupService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DescaTestModule],
                declarations: [VulnerableGroupComponent],
                providers: [
                    VulnerableGroupService
                ]
            })
            .overrideTemplate(VulnerableGroupComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(VulnerableGroupComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(VulnerableGroupService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new VulnerableGroup(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.vulnerableGroups[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
